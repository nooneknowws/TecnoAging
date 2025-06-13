require("dotenv-safe").config();
const jwt = require('jsonwebtoken');
var http = require('http');
const express = require('express');
const cors = require('cors');
const httpProxy = require('express-http-proxy')
const app = express()
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser')
var logger = require('morgan');
const helmet = require('helmet');
const axios = require('axios'); 

//configurações
app.use(logger('dev'));
app.use(cors());
app.use(helmet());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json());

//DEFs
const authServiceProxy = httpProxy(process.env.AUTH_SERVICE_URL);
const pacientesServiceProxy = httpProxy(process.env.PACIENTES_SERVICE_URL);
const tecnicosServiceProxy = httpProxy(process.env.TECNICOS_SERVICE_URL);
const formsServiceProxy = httpProxy(process.env.FORMS_SERVICE_URL);
//Funções
async function verifyJWT(req, res, next) {
    const authHeader = req.headers['authorization'];
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return res.status(401).json({ 
            auth: false, 
            message: 'Cabeçalho Authorization ausente ou mal formatado' 
        });
    }

    const token = authHeader.split(' ')[1];
    if (!token) {
        return res.status(401).json({ 
            auth: false, 
            message: 'Token não fornecido' 
        });
    }

    try {
        const response = await axios.post(
            `${process.env.AUTH_SERVICE_URL}/api/auth/verify-jwt`,
            {},
            { 
                headers: { 
                    'Authorization': `Bearer ${token}` 
                } 
            }
        );
        console.log(response.data)

        if (response.data.valid) {
            req.userId = response.data.userId;
            next();
        } else {
            res.status(401).json({ 
                auth: false, 
                message: response.data.message || 'Token inválido' 
            });
        }
    } catch (error) {
        console.error('Verification error:', error);
        
        const statusCode = error.response?.status || 500;
        const errorMessage = error.response?.data?.message || 'Erro na verificação do token';
        
        res.status(statusCode).json({ 
            auth: false, 
            message: errorMessage 
        });
    }
}
// AUTH

app.post('/api/auth/login', (req, res, next) => {
    authServiceProxy(req, res, next);
})

app.post('/api/auth/logout', (req, res, next) => {
    authServiceProxy(req, res, next);
})

app.post('/api/auth/verify-jwt', (req, res, next) => {
    authServiceProxy(req, res, next);
})

// PACIENTES

// cadastro
app.post('/api/pacientes', (req, res, next) => {
    pacientesServiceProxy(req, res, next);
})
//busca por ID
app.get('/api/pacientes/:id', verifyJWT, (req, res, next) => {
    pacientesServiceProxy(req,res,next);
})
//listagem geral
app.get('/api/pacientes', verifyJWT, (req, res, next) => {
    pacientesServiceProxy(req,res,next);
})
//alterar dados
app.put('/api/pacientes/:id', verifyJWT, (req, res, next) => {
    pacientesServiceProxy(req, res, next);
})
// TECNICOS
// cadastro
app.post('/api/tecnicos', (req, res, next) => {
    tecnicosServiceProxy(req, res, next)
})
// listagem geral
app.get('/api/tecnicos', verifyJWT, (req, res, next) => {
    tecnicosServiceProxy(req, res, next)
})
// busca por id
app.get('/api/tecnicos/:id', verifyJWT, (req, res, next) => {
    tecnicosServiceProxy(req, res, next);
})
// alterar tecnico
app.put('/api/tecnicos/:id', verifyJWT, (req, res, next) => {
    tecnicosServiceProxy(req, res, next);
})
// desativar tecnico
app.patch('/api/tecnicos/{id}/desativar', verifyJWT, (req, res, next) => {
    tecnicosServiceProxy(req, res, next);
})
// reativar tecnico
app.patch('/api/tecnicos/{id}/ativar', verifyJWT, (req, res, next) => {
    tecnicosServiceProxy(req, res, next);
})

// FORMS
// listagem geral
app.get('/api/formularios/', verifyJWT, (req,  res,  next) => {
    formsServiceProxy(req, res, next);
})
// busca por ID
app.get('/api/formularios/:id', verifyJWT, (req,  res,  next) => {
    formsServiceProxy(req, res, next);
})
// registro de avaliações
app.post('/api/avaliacoes/forms', verifyJWT, (req, res, next) => {
    formsServiceProxy(req,res,next);
})
// avaliações por paciente ID
app.get('/api/avaliacoes/respostas/paciente/:id', verifyJWT, (req, res, next) => {
    formsServiceProxy(req,res,next);
})
app.get('/api/avaliacoes/respostas/paciente/cpf/:cpf', verifyJWT, (req, res, next) => {
    formsServiceProxy(req, res, next);
});
// avaliações por tecnico ID
app.get('/api/avaliacoes/respostas/tecnico/:id', verifyJWT, (req, res, next) => {
    formsServiceProxy(req,res,next);
})
// avaliações por avaliacao ID
app.get('/api/avaliacoes/avaliacao/:id', verifyJWT, (req, res, next) => {
    formsServiceProxy(req,res,next);
})

var server = http.createServer(app);

server.listen(process.env.PORT, '0.0.0.0', () => {
    console.log(`Gateway online rodando na porta: ${process.env.PORT}`);
});
