require("dotenv-safe").config();
const jwt = require('jsonwebtoken');
var http = require('http');
const express = require('express');
const httpProxy = require('express-http-proxy')
const app = express()
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser')
var logger = require('morgan');
const helmet = require('helmet');
//configurações
app.use(logger('dev'));
app.use(helmet());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use( bodyParser.urlencoded({ extended: false }))
app.use ( bodyParser.json() );

//DEFs
const authServiceProxy = httpProxy(process.env.AUTH_SERVICE_URL, {
    proxyReqBodyDecorator: function (bodyContent, srcReq) {
        try {
            retBody = {};
            retBody.login = bodyContent.login;
            retBody.senha = bodyContent.senha;
            bodyContent = retBody;
            console.log(retBody)
        }
        catch(e) {
            console.log( '- ERRO: ' + e);
        }
        return bodyContent
    },
    proxyReqOptDecorator: function (proxyReqOpts, srcReq){
        proxyReqOpts.headers['Content-Type'] = 'application/json';
        proxyReqOpts.method = 'POST';
        return proxyReqOpts;

    },
    userResDecorator: function(proxyRes, proxyResData, userReq, userRes) {
        console.log("Status Code do Serviço Autenticação:", proxyRes.statusCode);
        console.log("Resposta do Serviço Autenticação:", proxyResData.toString());
    
        if (proxyRes.statusCode == 200) {
            try {
                const str = Buffer.from(proxyResData).toString('utf-8');
                console.log("Dados JSON convertidos:", str); // Debug do JSON
                const objBody = JSON.parse(str);
                const id = objBody.id;
                const token = jwt.sign({ id }, process.env.SECRET, {
                    expiresIn: 300 // expira em 5 minutos
                });
                userRes.status(200);
                return { auth: true, token: token, data: objBody };
            } catch (error) {
                console.error("Erro ao manipular dados JSON:", error);
                userRes.status(500);
                return { message: "Erro interno no servidor" };
            }
        } else {
            userRes.status(401);
            return { message: 'Login inválido!' };
        }
    }
    
});
const pacientesServiceProxy = httpProxy(process.env.PACIENTES_SERVICE_URL);
const tecnicosServiceProxy = httpProxy(process.env.TECNICOS_SERVICE_URL);
const formsServiceProxy = httpProxy('http://localhost:5000');

function verifyJWT(req, res, next) {
    const token = req.headers['x-access-token'];
    if (!token) 
        return res.status(401).json({ auth: false, message: 'token não fornecido'});
    
    jwt.verify(token, process.env.SECRET, function(err,decoded) {
        if (err)
            return res.status(500).json({ auth: false, message: 'falha ao autenticar token.' });

        req.userId = decoded.id;
        next();
    });
}

app.post('/login', (req, res, next) => {
    authServiceProxy(req, res, next);
})

app.post('/logout', function(req, res){
    //isso aqui só anula o login, bem simples
    res.json({ auth: false, token: null });
})

//aqui vai os HTTP da vida, que comunica com os MS->
app.get ('/api/formularios/', (req, res, next) => {
    formsServiceProxy(req,res,next);
})


//criar o servidor na porta 3000
var server = http.createServer(app);
server.listen(3000);

//pra testar no postman
//manda um post pra localhost:3000/login no body vc coloca x-www-form-urlencoded como tipo de dado e manda 2 key, user e password ambas admin
//ai ele vai gerar um token, vc passa esse token no get pra localhost:3000/xxxx <- depende do MS q tu quer acessar
//jogar o token no HEADER como x-access-token na key e o value tu copia o token q foi gerado no login.
//ele vai retornar o conteudo do DB.json dos MS em json-server, não esquece de executar os db.json com  json-server --watch db.json --port 5002
//só abrir um console na pasta do MS e digitar a porta de acordo com o MS, a gente tem q padronizar isso dps.