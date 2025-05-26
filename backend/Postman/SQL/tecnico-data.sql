--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4 (Debian 17.4-1.pgdg120+2)
-- Dumped by pg_dump version 17.4 (Debian 17.4-1.pgdg120+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: db-tecnicos; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "db-tecnicos" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE "db-tecnicos" OWNER TO postgres;

\encoding SQL_ASCII
\connect -reuse-previous=on "dbname='db-tecnicos'"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: tecnico; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tecnico (id, cpf, data_nasc, bairro, cep, complemento, logradouro, municipio, numero, uf, idade, nome, password_hash, salt, senha, sexo, telefone, ativo, matricula) FROM stdin;
1	10594113938	1996-06-13	Centro	83420053	casa 2	Rua Roseniro Alves de Paula	Quatro Barras	91	PR	28	Thaly	zu/sH9N2CfBseggA9CUuEt4CFxDMjOMRguPmQhq7UgE=	nZB/nNhE03y3xICe0xQBMA==	1234	M	41920032665	t	3341123
2	12345678901	1998-07-16	Residencial Paraíso	78734005	casa	Rua Ozélia Daria Mello	Rondonópolis	334	MT	26	asdsa	+fRDDv12CHMD59iTKt8bsHqro6pmBtJ5FAziIn1gU9Y=	0G84V2ps3CAjHZlInxjVmw==	1234	M	31212321312	t	12321312
4	12343312343	2025-05-06	Residencial Paraíso	78734005	casa	Rua Ozélia Daria Mello	Rondonópolis	22	MT	0	testeee	80Oqx3YjmyD8rxqMhbtz37O5I2n6la8jRiRZdgDj0WA=	eWruF/ohCPNJuLqMsF9axw==	1234	M	12321321321	t	12312321
\.

-- Password: Tecnico#1
COPY public.tecnico (id, cpf, data_nasc, bairro, cep, complemento, logradouro, municipio, numero, uf, idade, nome, password_hash, salt, senha, sexo, telefone, ativo, matricula) FROM stdin;
5	12345678911	1985-03-22	Centro	80000000	Apto 101	Rua Principal	Curitiba	100	PR	38	João Silva	zu/sH9N2CfBseggA9CUuEt4CFxDMjOMRguPmQhq7UgE=	nZB/nNhE03y3xICe0xQBMA==	Tecnico#1	M	41999999999	t	12345
\.

-- Password: Abcd1234
COPY public.tecnico (id, cpf, data_nasc, bairro, cep, complemento, logradouro, municipio, numero, uf, idade, nome, password_hash, salt, senha, sexo, telefone, ativo, matricula) FROM stdin;
6	98765432100	1990-07-15	Jardim Botânico	80210000	Casa 2	Rua das Flores	Curitiba	200	PR	33	Maria Oliveira	+fRDDv12CHMD59iTKt8bsHqro6pmBtJ5FAziIn1gU9Y=	0G84V2ps3CAjHZlInxjVmw==	Abcd1234	F	41988888888	t	23456
\.

-- Password: Tech@2023
COPY public.tecnico (id, cpf, data_nasc, bairro, cep, complemento, logradouro, municipio, numero, uf, idade, nome, password_hash, salt, senha, sexo, telefone, ativo, matricula) FROM stdin;
7	55544433322	1988-11-30	Batel	80420000	Sala 501	Av. República Argentina	Curitiba	500	PR	35	Carlos Souza	80Oqx3YjmyD8rxqMhbtz37O5I2n6la8jRiRZdgDj0WA=	eWruF/ohCPNJuLqMsF9axw==	Tech@2023	M	41977777777	t	34567
\.

--
-- Name: tecnico_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tecnico_id_seq', 4, true);


--
-- PostgreSQL database dump complete
--

