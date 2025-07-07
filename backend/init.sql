
CREATE DATABASE "db-pacientes" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
CREATE DATABASE "db-forms" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
CREATE DATABASE "db-testes" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
CREATE DATABASE "db-analises" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
CREATE DATABASE "db-avaliacoes" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
CREATE DATABASE "db-tecnicos" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';

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

ALTER DATABASE "db-forms" OWNER TO postgres;
ALTER DATABASE "db-pacientes" OWNER TO postgres;
ALTER DATABASE "db-testes" OWNER TO postgres;
ALTER DATABASE "db-analises" OWNER TO postgres;
ALTER DATABASE "db-avaliacoes" OWNER TO postgres;
ALTER DATABASE "db-tecnicos" OWNER TO postgres;
ALTER DATABASE "postgres" OWNER TO postgres;

