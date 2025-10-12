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
// Aumentar limite para uploads de imagem
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: false, limit: '10mb' }));
app.use(cookieParser());
app.use(bodyParser.urlencoded({ extended: false, limit: '10mb' }))
app.use(bodyParser.json({ limit: '10mb' }));

//DEFs
const authServiceProxy = httpProxy(process.env.AUTH_SERVICE_URL);
const pacientesServiceProxy = httpProxy(process.env.PACIENTES_SERVICE_URL, {
    // Configurações específicas para upload de imagens
    limit: '10mb',
    timeout: 90000, // Aumentado para 90 segundos
    proxyReqOptDecorator: function(proxyReqOpts, srcReq) {
        // Headers customizados se necessário
        proxyReqOpts.headers = proxyReqOpts.headers || {};
        return proxyReqOpts;
    },
    proxyErrorHandler: function(err, res, next) {
        console.error('Proxy error for pacientes:', err.message);
        if (err.code === 'ECONNRESET' || err.code === 'ETIMEDOUT') {
            res.status(408).json({ 
                error: 'Request timeout', 
                message: 'O servidor demorou muito para responder' 
            });
        } else {
            next(err);
        }
    }
});
const tecnicosServiceProxy = httpProxy(process.env.TECNICOS_SERVICE_URL, {
    // Configurações específicas para upload de imagens
    limit: '10mb',
    timeout: 90000, // Aumentado para 90 segundos
    proxyReqOptDecorator: function(proxyReqOpts, srcReq) {
        // Headers customizados se necessário
        proxyReqOpts.headers = proxyReqOpts.headers || {};
        return proxyReqOpts;
    },
    proxyErrorHandler: function(err, res, next) {
        console.error('Proxy error for tecnicos:', err.message);
        if (err.code === 'ECONNRESET' || err.code === 'ETIMEDOUT') {
            res.status(408).json({ 
                error: 'Request timeout', 
                message: 'O servidor demorou muito para responder' 
            });
        } else {
            next(err);
        }
    }
});
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
                },
                timeout: 30000 // Timeout para verificação de JWT
            }
        );
        console.log(response.data)

        if (response.data.valid) {
            req.userId = response.data.userId;
            // Add userId to headers for downstream services
            req.headers['x-user-id'] = response.data.userId;
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

app.post('/api/auth/enviar-codigo', (req, res, next) => {
    authServiceProxy(req, res, next);
})

app.post('/api/auth/reset-password', (req, res, next) => {
    authServiceProxy(req, res, next);
})

// PACIENTES

// cadastro
app.post('/api/pacientes', (req, res, next) => {
    pacientesServiceProxy(req, res, next);
})
//busca por ID
app.get('/api/pacientes/:id', verifyJWT, (req, res, next) => {
    console.log(`Buscando dados do paciente ${req.params.id}`);
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

// ROTAS DE FOTO - PACIENTES
// Upload da foto do paciente
app.post('/api/pacientes/:id/foto', verifyJWT, (req, res, next) => {
    console.log(`Proxy para upload de foto do paciente ${req.params.id}`);
    pacientesServiceProxy(req, res, next);
})

// Buscar foto do paciente
app.get('/api/pacientes/:id/foto', verifyJWT, (req, res, next) => {
    console.log(`Proxy para buscar foto do paciente ${req.params.id}`);
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
    console.log(`Buscando dados do técnico ${req.params.id}`);
    tecnicosServiceProxy(req, res, next);
})
// alterar tecnico
app.put('/api/tecnicos/:id', verifyJWT, (req, res, next) => {
    tecnicosServiceProxy(req, res, next);
})
// desativar tecnico
app.patch('/api/tecnicos/:id/desativar', verifyJWT, (req, res, next) => {
    tecnicosServiceProxy(req, res, next);
})
// reativar tecnico
app.patch('/api/tecnicos/:id/ativar', verifyJWT, (req, res, next) => {
    tecnicosServiceProxy(req, res, next);
})

// ROTAS DE FOTO - TECNICOS
// Upload da foto do técnico
app.post('/api/tecnicos/:id/foto', verifyJWT, (req, res, next) => {
    console.log(`Proxy para upload de foto do técnico ${req.params.id}`);
    tecnicosServiceProxy(req, res, next);
})

// Buscar foto do técnico
app.get('/api/tecnicos/:id/foto', verifyJWT, (req, res, next) => {
    console.log(`Proxy para buscar foto do técnico ${req.params.id}`);
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
// criar formulário cadastrável
app.post('/api/formularios/cadastro', verifyJWT, (req, res, next) => {
    formsServiceProxy(req, res, next);
})
// atualizar formulário
app.put('/api/formularios/:id', verifyJWT, (req, res, next) => {
    formsServiceProxy(req, res, next);
})
// deletar formulário
app.delete('/api/formularios/:id', verifyJWT, (req, res, next) => {
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
// editar avaliação por ID
app.put('/api/avaliacoes/forms/update/:id', verifyJWT, (req, res, next) => {
    console.log(`Atualizando avaliação ID: ${req.params.id}`);
    formsServiceProxy(req, res, next);
})

// Middleware para tratamento de erros de upload
app.use((error, req, res, next) => {
    if (error instanceof SyntaxError && error.status === 400 && 'body' in error) {
        console.error('Erro de parsing JSON:', error.message);
        return res.status(400).json({ 
            error: 'JSON mal formatado ou muito grande',
            message: 'Verifique o formato da imagem e o tamanho do arquivo'
        });
    }
    
    // Tratamento específico para timeouts
    if (error.code === 'ETIMEDOUT' || error.code === 'ECONNRESET') {
        console.error('Timeout error:', error.message);
        return res.status(408).json({
            error: 'Request timeout',
            message: 'O servidor demorou muito para responder'
        });
    }
    
    next();
});

var server = http.createServer(app);

server.listen(process.env.PORT, '0.0.0.0', () => {
    console.log(`Gateway online rodando na porta: ${process.env.PORT}`);
});