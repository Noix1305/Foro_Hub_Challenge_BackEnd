ALTER TABLE usuarios ADD COLUMN activo BOOLEAN DEFAULT true;

UPDATE usuarios SET activo = true;
