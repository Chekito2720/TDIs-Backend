-- Admin user (password: admin123)
INSERT INTO usuarios (id, email, password, nombre, apellidos, tipo_usuario, activo, created_at, updated_at)
SELECT * FROM (VALUES
    (gen_random_uuid(), 'admin@uteq.edu.mx', '$2a$10$A5ioBCADMpmR6l.WdwzuROy77Ezg8fWB1FDHgqSF1IS6I4ubyeAGK',
     'Admin', 'Sistema', 'ADMINISTRADOR', true, NOW(), NOW())
) AS v
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@uteq.edu.mx');

-- Test students (password = matricula)
INSERT INTO usuarios (id, matricula, password, nombre, apellidos, tipo_usuario, activo, created_at, updated_at)
SELECT * FROM (VALUES
    (gen_random_uuid(), '2026191005', '$2a$10$PFoOdwWReMECRhxOZL7PLey4r/ta1BnjkrqlPOQ9Vx3l64FXbJdEW', 'Juan Pablo', 'Pérez López', 'ALUMNO', true, NOW(), NOW()),
    (gen_random_uuid(), '2024396177', '$2a$10$2Y4ri35oALDzYOlE2jdUceffsuXryCGQgqsxygTZIlsV8hDLLZ9Z2', 'María', 'García Hernández', 'ALUMNO', true, NOW(), NOW()),
    (gen_random_uuid(), '2025192003', '$2a$10$wnvGYgZyiX.p/dDahjw13.bRO9bmpFEMZBQNQhyFi.jl1OdXelSvW', 'Carlos', 'Martínez Ruiz', 'ALUMNO', true, NOW(), NOW()),
    (gen_random_uuid(), '2024185008', '$2a$10$jkTX0h3lt6Xw0MlCZS/Vp.BwKmJAqWJvWMpa0iP/gdpVjs4s1BfTa', 'Ana Sofía', 'Torres Mendoza', 'ALUMNO', true, NOW(), NOW())
) AS v
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE matricula = v.column2);
