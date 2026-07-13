INSERT INTO actividades (id, titulo, descripcion, eje, puntos_tdi, periodicidad, fecha_inicio, fecha_fin, activa, created_at, updated_at)
SELECT * FROM (VALUES
    (gen_random_uuid(), 'Apoyo Social - Croquton y acciones comunitarias', 'Participacion en actividades de apoyo a la comunidad: croquton, colectas y brigadas solidarias.', 'ENTORNO_SOCIAL', 3, 'UNICA', '2026-08-01'::date, '2026-08-15'::date, true, NOW(), NOW()),
    (gen_random_uuid(), 'Ponte en su Lugar: Museo de las Heridas no Visibles', 'Experiencia de sensibilizacion sobre salud mental y bienestar emocional en contextos de vulnerabilidad.', 'ENTORNO_SOCIAL', 1, 'UNICA', '2026-09-10'::date, NULL, true, NOW(), NOW()),
    (gen_random_uuid(), 'Defensa Urbana Femenina', 'Taller de autodefensa y seguridad personal para mujeres en entornos urbanos.', 'ENTORNO_SOCIAL', 4, 'UNICA', '2026-08-20'::date, '2026-08-20'::date, true, NOW(), NOW()),
    (gen_random_uuid(), 'CineClub', 'Proyeccion de peliculas con reflexion guiada sobre valores, diversidad cultural y sociedad.', 'PERSONAL', 2, 'MENSUAL', '2026-08-01'::date, '2026-12-15'::date, true, NOW(), NOW()),
    (gen_random_uuid(), 'Taller de Arte Urbano', 'Participacion en talleres de graffiti y muralismo.', 'PERSONAL', 3, 'UNICA', '2026-09-01'::date, '2026-09-30'::date, true, NOW(), NOW()),
    (gen_random_uuid(), 'Concierto Universitario', 'Asistencia a eventos musicales universitarios.', 'PERSONAL', 2, 'MENSUAL', '2026-08-01'::date, '2026-12-20'::date, true, NOW(), NOW()),
    (gen_random_uuid(), 'Torneo de Futbol Interuniversitario', 'Participacion en torneos deportivos.', 'DEPORTIVO', 4, 'UNICA', '2026-10-05'::date, '2026-10-05'::date, true, NOW(), NOW()),
    (gen_random_uuid(), 'Clases de Yoga al Aire Libre', 'Sesiones de yoga y meditacion.', 'DEPORTIVO', 2, 'SEMANAL', '2026-08-04'::date, '2026-12-15'::date, true, NOW(), NOW()),
    (gen_random_uuid(), 'Jornada de Vacunacion Influenza', 'Campana institucional de vacunacion preventiva contra influenza y otras enfermedades.', 'DEPORTIVO', 2, 'UNICA', '2026-11-10'::date, '2026-11-14'::date, true, NOW(), NOW()),
    (gen_random_uuid(), 'Voluntariado Social Externo', 'Servicio voluntario en organizaciones externas: casas hogar, albergues, fundaciones o comunidades.', 'TRASCENDENCIA', 4, 'UNICA', '2026-08-01'::date, NULL, true, NOW(), NOW()),
    (gen_random_uuid(), 'Conferencia de Liderazgo', 'Platicas sobre desarrollo personal y liderazgo.', 'TRASCENDENCIA', 3, 'SEMANAL', '2026-09-01'::date, '2026-12-10'::date, true, NOW(), NOW()),
    (gen_random_uuid(), 'Programa de Mentoria', 'Ser mentor de alumnos de nuevo ingreso.', 'TRASCENDENCIA', 5, 'UNICA', '2026-08-18'::date, '2026-12-20'::date, true, NOW(), NOW())
) AS v
WHERE NOT EXISTS (SELECT 1 FROM actividades);
