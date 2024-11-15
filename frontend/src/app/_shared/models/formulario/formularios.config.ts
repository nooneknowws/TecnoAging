import { TipoFormulario } from "../tipos.formulario.enum";
import { Formulario } from "./formulario";

//TODO: adequar aos formulários
export const FORMULARIOS_CONFIG: { [key in TipoFormulario]: Formulario } = {
    [TipoFormulario.SEDENTARISMO]: {
        id: 1,
        tipo: TipoFormulario.SEDENTARISMO,
        titulo: 'Avaliação de Sedentarismo',
        descricao: 'Avaliação do nível de sedentarismo do paciente',
        etapas: [
            {
                titulo: 'Atividade Moderada',
                descricao: 'Você consegue realizá-la conversando com dificuldade enquanto se movimenta e não vai conseguir cantar.',
                perguntas: [
                    {
                        texto: 'Quanto tempo por dia você realiza atividades moderadas?',
                        tipo: 'tempo',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Quantos dias por semana?',
                        tipo: 'dias',
                        resposta: '',
                        validacao: { required: true, min: 0, max: 7 }
                    }
                ]
            },
            {
                titulo: 'Atividade Vigorosa',
                descricao: 'Você não vai conseguir nem conversar. A sua respiração vai ser muito mais rápida que o normal e os batimentos do seu coração vão aumentar muito.',
                perguntas: [
                    {
                        texto: 'Quanto tempo por dia você realiza atividades vigorosas?',
                        tipo: 'tempo',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Quantos dias por semana?',
                        tipo: 'dias',
                        resposta: '',
                        validacao: { required: true, min: 0, max: 7 }
                    }
                ]
            },
            {
                titulo: 'Comportamento Sedentário',
                descricao: 'Quanto tempo do seu dia, enquanto você está acordado, você gasta sentado, reclinado ou deitado, assistindo televisão, no celular, em frente ao computador, realizando trabalhos manuais, dirigindo ou lendo?',
                perguntas: [
                    {
                        texto: 'Quanto tempo por dia?',
                        tipo: 'tempo',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Quantos dias por semana?',
                        tipo: 'dias',
                        resposta: '',
                        validacao: { required: true, min: 0, max: 7 }
                    }
                ]
            }
        ]
    },

    [TipoFormulario.PFS]: {
        id: 2,
        tipo: TipoFormulario.PFS,
        titulo: 'Formulário PFS',
        descricao: 'Avaliação do nível de fadiga física e mental após diferentes atividades',
        etapas: [
            {
                titulo: 'Atividade 1',
                descricao: 'Caminhar à lazer por 30 minutos.',
                perguntas: [
                    { texto: 'Fadiga Física (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Fadiga Mental (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Você realizou essa atividade no último mês?', tipo: 'radio', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Atividade 2',
                descricao: 'Caminhada rápida ou acelerada por 1 hora.',
                perguntas: [
                    { texto: 'Fadiga Física (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Fadiga Mental (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Você realizou essa atividade no último mês?', tipo: 'radio', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Atividade 3',
                descricao: 'Atividade doméstica leve por 1 hora.',
                perguntas: [
                    { texto: 'Fadiga Física (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Fadiga Mental (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Você realizou essa atividade no último mês?', tipo: 'radio', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Atividade 4',
                descricao: 'Trabalho pesado de jardinagem ou ao ar livre por 1 hora.',
                perguntas: [
                    { texto: 'Fadiga Física (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Fadiga Mental (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Você realizou essa atividade no último mês?', tipo: 'radio', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Atividade 5',
                descricao: 'Assistir à TV por 2 horas.',
                perguntas: [
                    { texto: 'Fadiga Física (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Fadiga Mental (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Você realizou essa atividade no último mês?', tipo: 'radio', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Atividade 6',
                descricao: 'Sentar-se em silêncio por 1 hora.',
                perguntas: [
                    { texto: 'Fadiga Física (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Fadiga Mental (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Você realizou essa atividade no último mês?', tipo: 'radio', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Atividade 7',
                descricao: 'Treinamento de força - intensidade moderada a alta por 30 minutos.',
                perguntas: [
                    { texto: 'Fadiga Física (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Fadiga Mental (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Você realizou essa atividade no último mês?', tipo: 'radio', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Atividade 8',
                descricao: 'Participar de um evento social por 2 horas.',
                perguntas: [
                    { texto: 'Fadiga Física (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Fadiga Mental (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Você realizou essa atividade no último mês?', tipo: 'radio', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Atividade 9',
                descricao: 'Atividade recreativa moderada por 1 hora.',
                perguntas: [
                    { texto: 'Fadiga Física (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Fadiga Mental (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Você realizou essa atividade no último mês?', tipo: 'radio', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Atividade 10',
                descricao: 'Atividade aeróbica leve por 30 minutos.',
                perguntas: [
                    { texto: 'Fadiga Física (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Fadiga Mental (0-5)', tipo: 'numero', resposta: '', validacao: { required: true } },
                    { texto: 'Você realizou essa atividade no último mês?', tipo: 'radio', resposta: '', validacao: { required: true } }
                ]
            }
        ]
    },

    [TipoFormulario.IVCF20]: {
        id: 3,
        tipo: TipoFormulario.IVCF20,
        titulo: 'Índice de Vulnerabilidade Clínico-Funcional (IVCF-20)',
        descricao: 'Avaliação multidimensional do grau de vulnerabilidade clínica e funcional de idosos',
        etapas: [
            {
                titulo: 'Auto-percepção da Saúde',
                descricao: 'Avaliação geral da saúde comparada a outras pessoas da mesma idade.',
                perguntas: [
                    {
                        texto: 'Comparado com outras pessoas da sua idade, como você diria que está a sua saúde?',
                        tipo: 'radio',
                        opcoes: ['Excelente', 'Boa', 'Ruim'],
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Atividades de Vida Diária',
                descricao: 'Avaliação de atividades como compras, finanças, e pequenos trabalhos domésticos.',
                perguntas: [
                    {
                        texto: 'Você deixou de fazer compras por causa da sua saúde ou condição física?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você deixou de controlar seu dinheiro ou os gastos da casa por causa da sua saúde ou condição física?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você deixou de realizar pequenos trabalhos domésticos por causa da sua saúde ou condição física?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você deixou de tomar banho sozinho por causa da sua saúde ou condição física?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Cognição',
                descricao: 'Avaliação de possíveis sinais de problemas de memória.',
                perguntas: [
                    {
                        texto: 'Algum familiar ou amigo falou que você está ficando esquecido?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Este esquecimento está piorando nos últimos meses?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Este esquecimento está impedindo a realização de alguma atividade do cotidiano?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Humor',
                descricao: 'Avaliação de sintomas relacionados ao humor.',
                perguntas: [
                    {
                        texto: 'No último mês, você ficou com desânimo, tristeza ou desesperança?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'No último mês, você perdeu o interesse ou prazer em atividades anteriormente prazerosas?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Mobilidade',
                descricao: 'Avaliação de mobilidade e capacidade física.',
                perguntas: [
                    {
                        texto: 'Você é incapaz de elevar os braços acima do nível do ombro?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você é incapaz de segurar pequenos objetos?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você tem dificuldade para caminhar capaz de impedir a realização de alguma atividade do cotidiano?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            },
            {
                titulo: 'Comorbidades e Condições Múltiplas',
                descricao: 'Avaliação de condições relacionadas a múltiplas doenças ou hospitalizações.',
                perguntas: [
                    {
                        texto: 'Você tem problemas de visão que impedem a realização de atividades do cotidiano?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você tem problemas de audição que impedem a realização de atividades do cotidiano?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    },
                    {
                        texto: 'Você possui cinco ou mais doenças crônicas, uso regular de cinco ou mais medicamentos, ou internação recente nos últimos 6 meses?',
                        tipo: 'radio',
                        resposta: '',
                        validacao: { required: true }
                    }
                ]
            }
        ]
    },
    
    [TipoFormulario.MINIMENTAL]: {
        id: 4,
        tipo: TipoFormulario.MINIMENTAL,
        titulo: 'Mini Exame do Estado Mental (MEEM)',
        descricao: 'Avaliação cognitiva para identificar possíveis déficits cognitivos.',
        etapas: [
            {
                titulo: 'Orientação Temporal',
                descricao: 'Avaliação da capacidade de orientação no tempo.',
                perguntas: [
                    { texto: 'Qual é o ano atual?', tipo: 'texto', resposta: '', validacao: { required: true } },
                    { texto: 'Qual é a estação do ano atual?', tipo: 'texto', resposta: '', validacao: { required: true } },
                    { texto: 'Qual é o mês atual?', tipo: 'texto', resposta: '', validacao: { required: true } },
                    { texto: 'Qual é a data de hoje?', tipo: 'texto', resposta: '', validacao: { required: true } },
                    { texto: 'Que dia da semana é hoje?', tipo: 'texto', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Orientação Espacial',
                descricao: 'Avaliação da capacidade de orientação no espaço.',
                perguntas: [
                    { texto: 'Em que país estamos?', tipo: 'texto', resposta: '', validacao: { required: true } },
                    { texto: 'Em que estado estamos?', tipo: 'texto', resposta: '', validacao: { required: true } },
                    { texto: 'Em que cidade estamos?', tipo: 'texto', resposta: '', validacao: { required: true } },
                    { texto: 'Em que local estamos?', tipo: 'texto', resposta: '', validacao: { required: true } },
                    { texto: 'Em que andar estamos?', tipo: 'texto', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Memória Imediata',
                descricao: 'Teste de repetição de palavras.',
                perguntas: [
                    { texto: 'Repita as palavras ditas pelo avaliador (ex.: "bola", "cavalo", "navio").', tipo: 'texto', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Cálculo',
                descricao: 'Teste de subtrações sequenciais.',
                perguntas: [
                    { texto: 'Subtraia 7 de 100 e continue subtraindo 7 sucessivamente.', tipo: 'texto', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Memória de Evocação',
                descricao: 'Teste de memória de palavras mencionadas anteriormente.',
                perguntas: [
                    { texto: 'Quais eram as três palavras ditas anteriormente?', tipo: 'texto', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Nomeação',
                descricao: 'Identificação de objetos simples.',
                perguntas: [
                    { texto: 'Qual é o nome deste objeto (mostre um relógio)?', tipo: 'texto', resposta: '', validacao: { required: true } },
                    { texto: 'Qual é o nome deste objeto (mostre uma caneta)?', tipo: 'texto', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Repetição',
                descricao: 'Teste de repetição de frase.',
                perguntas: [
                    { texto: 'Repita a frase: "Nem aqui, nem ali, nem lá."', tipo: 'texto', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Comando',
                descricao: 'Realização de instruções simples.',
                perguntas: [
                    { texto: 'Pegue esta folha com a mão direita, dobre-a ao meio e coloque-a no chão.', tipo: 'texto', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Leitura e Obediência',
                descricao: 'Leitura e execução de comando.',
                perguntas: [
                    { texto: 'Leia a frase "Feche os olhos" e siga o comando.', tipo: 'texto', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Escrita',
                descricao: 'Escrita de uma frase.',
                perguntas: [
                    { texto: 'Escreva uma frase com sentido completo.', tipo: 'texto', resposta: '', validacao: { required: true } }
                ]
            },
            {
                titulo: 'Cópia do Desenho',
                descricao: 'Reprodução de um modelo gráfico.',
                perguntas: [
                    { texto: 'Copie o desenho apresentado (ex.: dois pentágonos interligados).', tipo: 'texto', resposta: '', validacao: { required: true } }
                ]
            }
        ]
    },    
    [TipoFormulario.FACTF]: {
        id: 5,
        tipo: TipoFormulario.FACTF,
        titulo: 'Functional Assessment of Cancer Therapy - Fatigue (FACT-F)',
        descricao: 'Avaliação da qualidade de vida e fadiga em pacientes com câncer',
        etapas: [
            {
                titulo: 'Bem-estar Físico',
                descricao: 'Avaliação do seu estado físico durante os últimos 7 dias',
                perguntas: [
                    { texto: 'Estou sem energia', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Fico enjoado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Por causa do meu estado físico, tenho dificuldade em atender às necessidades da minha família', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho dores', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me incomodado(a) pelos efeitos secundários do tratamento', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me doente', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho que me deitar durante o dia', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } }
                ]
            },
            {
                titulo: 'Bem-estar Social/Familiar',
                descricao: 'Avaliação do seu relacionamento com família e amigos durante os últimos 7 dias',
                perguntas: [
                    { texto: 'Sinto que tenho uma boa relação com os meus amigos', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Recebo apoio emocional da minha família', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Recebo apoio dos meus amigos', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'A minha família aceita a minha doença', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou satisfeito(a) com a maneira como a minha família fala sobre a minha doença', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me próximo(a) do(a) meu (minha) parceiro(a) (ou da pessoa que me dá maior apoio)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou satisfeito(a) com a minha vida sexual', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: false } }
                ]
            },
            {
                titulo: 'Bem-estar Emocional',
                descricao: 'Avaliação do seu estado emocional durante os últimos 7 dias',
                perguntas: [
                    { texto: 'Sinto-me triste', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou satisfeito(a) com a maneira como enfrento a minha doença', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou perdendo a esperança na luta contra a minha doença', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me nervoso(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou preocupado(a) com a idéia de morrer', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou preocupado(a) que o meu estado venha a piorar', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } }
                ]
            },
            {
                titulo: 'Bem-estar Funcional',
                descricao: 'Avaliação da sua capacidade funcional durante os últimos 7 dias',
                perguntas: [
                    { texto: 'Sou capaz de trabalhar (inclusive em casa)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me realizado(a) com o meu trabalho (inclusive em casa)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sou capaz de sentir prazer em viver', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Aceito a minha doença', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Durmo bem', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Gosto das coisas que normalmente faço para me divertir', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou satisfeito(a) com a qualidade da minha vida neste momento', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } }
                ]
            },
            {
                titulo: 'Preocupações Adicionais',
                descricao: 'Avaliação de preocupações específicas durante os últimos 7 dias',
                perguntas: [
                    { texto: 'Sinto-me fatigado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto fraqueza generalizada', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me sem forças', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sinto-me cansado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho dificuldade em começar as coisas porque estou cansado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho dificuldade em acabar as coisas porque estou cansado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho energia', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Sou capaz de fazer as minhas atividades normais', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Preciso (de) dormir durante o dia', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou cansado(a) demais para comer', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Preciso de ajuda para fazer as minhas atividades normais', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Estou frustrado(a) por estar cansado(a) demais para fazer as coisas que quero', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } },
                    { texto: 'Tenho que limitar as minhas atividades sociais por estar cansado(a)', tipo: 'radio', resposta: '', opcoes: ['Nem um pouco', 'Um pouco', 'Mais ou menos', 'Muito', 'Muitíssimo'], validacao: { required: true } }
                ]
            }
        ]
    }
    ,
};
