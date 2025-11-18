-- Script para atualizar formulário IPAQ (sedentarismo) no banco de dados
-- Execute este script após fazer backup do banco

BEGIN;

-- Atualizar formulário para habilitar cálculo de pontuação
UPDATE public.formulario
SET
    descricao = 'IPAQ - Questionário Internacional de Atividade Física (versão curta)',
    titulo = 'IPAQ - Nível de Atividade Física',
    calcula_pontuacao = true,
    formula_custom = 'IPAQ_MET'
WHERE id = 4;

-- Adicionar etapa de Caminhada (ID 107)
INSERT INTO public.etapa (id, descricao, titulo, formulario_id, tipo_calculo, formula_custom)
VALUES (107, 'Pense em todas as atividades VIGOROSAS que você fez na ÚLTIMA SEMANA. Exemplos: caminhada rápida, caminhada recreativa, caminhada com cachorro.', 'Caminhada', 4, NULL, NULL);

-- Atualizar descrições das etapas existentes
UPDATE public.etapa
SET descricao = 'Pense em todas as atividades MODERADAS que você fez na ÚLTIMA SEMANA. Exemplos: pedalar em ritmo regular, carregar pesos leves, dançar. NÃO inclua caminhada.'
WHERE id = 24;

UPDATE public.etapa
SET descricao = 'Pense em todas as atividades VIGOROSAS que você fez na ÚLTIMA SEMANA. Exemplos: correr, fazer exercícios aeróbicos intensos, jogar futebol, carregar pesos pesados.'
WHERE id = 25;

UPDATE public.etapa
SET descricao = 'Quanto tempo você fica sentado(a) durante um dia de SEMANA? Considere tempo sentado(a) no trabalho, em casa, estudando, em deslocamentos, visitando amigos, lendo, assistindo TV.'
WHERE id = 26;

-- Adicionar perguntas para Caminhada (Etapa 107)
INSERT INTO public.pergunta (id, texto, tipo, max, min, required, etapa_id, tipo_pontuacao, pontos_minimos, pontos_maximos)
VALUES
(107, 'Em quantos dias da última semana você CAMINHOU por pelo menos 10 minutos contínuos?', 'numero', 7, 0, true, 107, 'VALOR_DIRETO', 0, 7),
(108, 'Nos dias em que você caminhou, quanto tempo por dia você gastou caminhando? (HH:MM)', 'tempo', NULL, NULL, true, 107, 'FORMULA', 0, 1440);

-- Atualizar perguntas da Atividade Moderada
UPDATE public.pergunta
SET
    texto = 'Em quantos dias da última semana você fez atividades MODERADAS por pelo menos 10 minutos contínuos?',
    required = true,
    tipo_pontuacao = 'VALOR_DIRETO',
    pontos_minimos = 0,
    pontos_maximos = 7
WHERE id = 48;

UPDATE public.pergunta
SET
    texto = 'Nos dias em que você fez atividades moderadas, quanto tempo por dia você gastou fazendo essas atividades? (HH:MM)',
    required = true,
    tipo_pontuacao = 'FORMULA',
    pontos_minimos = 0,
    pontos_maximos = 1440
WHERE id = 49;

-- Atualizar perguntas da Atividade Vigorosa
UPDATE public.pergunta
SET
    texto = 'Em quantos dias da última semana você fez atividades VIGOROSAS por pelo menos 10 minutos contínuos?',
    max = 7,
    min = 0,
    required = true,
    tipo_pontuacao = 'VALOR_DIRETO',
    pontos_minimos = 0,
    pontos_maximos = 7
WHERE id = 50;

UPDATE public.pergunta
SET
    texto = 'Nos dias em que você fez atividades vigorosas, quanto tempo por dia você gastou fazendo essas atividades? (HH:MM)',
    required = true,
    tipo_pontuacao = 'FORMULA',
    pontos_minimos = 0,
    pontos_maximos = 1440
WHERE id = 51;

-- Atualizar perguntas do Comportamento Sedentário
UPDATE public.pergunta
SET
    texto = 'Quanto tempo no total você gasta sentado(a) durante um dia de SEMANA? (HH:MM)',
    max = NULL,
    min = NULL,
    required = true,
    tipo_pontuacao = 'FORMULA',
    pontos_minimos = 0,
    pontos_maximos = 1440
WHERE id = 52;

UPDATE public.pergunta
SET
    texto = 'Quanto tempo no total você gasta sentado(a) durante um dia de FIM DE SEMANA? (HH:MM)',
    max = NULL,
    min = NULL,
    required = true,
    tipo_pontuacao = 'FORMULA',
    pontos_minimos = 0,
    pontos_maximos = 1440
WHERE id = 53;

COMMIT;

-- Verificar as alterações
SELECT 'Formulário atualizado:' as info;
SELECT id, titulo, calcula_pontuacao, formula_custom FROM public.formulario WHERE id = 4;

SELECT 'Etapas do IPAQ:' as info;
SELECT id, titulo FROM public.etapa WHERE formulario_id = 4 ORDER BY id;

SELECT 'Perguntas do IPAQ:' as info;
SELECT p.id, p.texto, e.titulo as etapa
FROM public.pergunta p
JOIN public.etapa e ON p.etapa_id = e.id
WHERE e.formulario_id = 4
ORDER BY e.id, p.id;
