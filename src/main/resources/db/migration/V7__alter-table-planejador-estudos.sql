-- Passo 1: Remover a chave estrangeira existente que depende da constraint
ALTER TABLE palenajor_estudos
DROP FOREIGN KEY FK7tt5nhh0nmfjikm9axk8y5flo;

-- Passo 2: Remover a constraint existente
ALTER TABLE palenajor_estudos
DROP INDEX UK2t82hpmuvl966v2vg7djw2nvr;

-- Passo 3: Adicionar a nova constraint de chave estrangeira
ALTER TABLE palenajor_estudos
ADD CONSTRAINT UK2t82hpmuvl966v2vg7djw2nvr
FOREIGN KEY (assunto_id)
REFERENCES assuntos_materia(id);
