INSERT INTO actividades (id, titulo, descripcion, eje, puntos_tdi, temporalidad, activa, created_at, updated_at) VALUES
    (gen_random_uuid(), 'Jornada de Limpieza Comunitaria', 'Participar en jornadas de limpieza en tu comunidad', 'ENTORNO_SOCIAL', 20, 'Semestral', true, NOW(), NOW()),
    (gen_random_uuid(), 'Visita a Casa Hogar', 'Apoyo en actividades recreativas en casas hogar', 'ENTORNO_SOCIAL', 30, 'Mensual', true, NOW(), NOW()),
    (gen_random_uuid(), 'Taller de Arte Urbano', 'Participacion en talleres de graffiti y muralismo', 'CULTURAL', 25, 'Semestral', true, NOW(), NOW()),
    (gen_random_uuid(), 'Concierto Universitario', 'Asistencia a eventos musicales universitarios', 'CULTURAL', 15, 'Mensual', true, NOW(), NOW()),
    (gen_random_uuid(), 'Torneo de Futbol Interuniversitario', 'Participacion en torneos deportivos', 'DEPORTIVO', 30, 'Anual', true, NOW(), NOW()),
    (gen_random_uuid(), 'Clases de Yoga al Aire Libre', 'Sesiones de yoga y meditacion', 'DEPORTIVO', 20, 'Semanal', true, NOW(), NOW()),
    (gen_random_uuid(), 'Conferencia de Liderazgo', 'Platicas sobre desarrollo personal y liderazgo', 'TRASCENDENCIA', 25, 'Semestral', true, NOW(), NOW()),
    (gen_random_uuid(), 'Programa de Mentoria', 'Ser mentor de alumnos de nuevo ingreso', 'TRASCENDENCIA', 40, 'Anual', true, NOW(), NOW());
