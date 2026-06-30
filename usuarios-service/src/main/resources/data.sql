INSERT INTO usuarios (id, email, password, nombre, apellidos, tipo_usuario, activo, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'admin@uteq.edu.mx', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'Admin', 'Sistema', 'ADMINISTRADOR', true, NOW(), NOW())
ON CONFLICT (email) DO NOTHING;
