INSERT INTO actividades (id, titulo, descripcion, eje, puntos_tdi, temporalidad, activa, created_at, updated_at)
SELECT * FROM (VALUES
    (gen_random_uuid(), 'Apoyo Social — Croquetón y acciones comunitarias', 'Participación en actividades de apoyo a la comunidad: croquetón, colectas y brigadas solidarias.', 'ENTORNO_SOCIAL', 3, 'Semestral', true, NOW(), NOW()),
    (gen_random_uuid(), 'Ponte en su Lugar: Museo de las Heridas no Visibles', 'Experiencia de sensibilización sobre salud mental y bienestar emocional en contextos de vulnerabilidad.', 'ENTORNO_SOCIAL', 1, 'Semestral', true, NOW(), NOW()),
    (gen_random_uuid(), 'Defensa Urbana Femenina', 'Taller de autodefensa y seguridad personal para mujeres en entornos urbanos.', 'ENTORNO_SOCIAL', 4, 'Semestral', true, NOW(), NOW()),
    (gen_random_uuid(), 'CineClub', 'Proyección de películas con reflexión guiada sobre valores, diversidad cultural y sociedad.', 'CULTURAL', 2, 'Mensual', true, NOW(), NOW()),
    (gen_random_uuid(), 'Taller de Arte Urbano', 'Participación en talleres de graffiti y muralismo.', 'CULTURAL', 3, 'Semestral', true, NOW(), NOW()),
    (gen_random_uuid(), 'Concierto Universitario', 'Asistencia a eventos musicales universitarios.', 'CULTURAL', 2, 'Mensual', true, NOW(), NOW()),
    (gen_random_uuid(), 'Torneo de Futbol Interuniversitario', 'Participación en torneos deportivos.', 'DEPORTIVO', 4, 'Anual', true, NOW(), NOW()),
    (gen_random_uuid(), 'Clases de Yoga al Aire Libre', 'Sesiones de yoga y meditación.', 'DEPORTIVO', 2, 'Semanal', true, NOW(), NOW()),
    (gen_random_uuid(), 'Jornada de Vacunación Influenza', 'Campaña institucional de vacunación preventiva contra influenza y otras enfermedades.', 'DEPORTIVO', 2, 'Anual', true, NOW(), NOW()),
    (gen_random_uuid(), 'Voluntariado Social Externo', 'Servicio voluntario en organizaciones externas: casas hogar, albergues, fundaciones o comunidades.', 'TRASCENDENCIA', 4, 'Libre', true, NOW(), NOW()),
    (gen_random_uuid(), 'Conferencia de Liderazgo', 'Pláticas sobre desarrollo personal y liderazgo.', 'TRASCENDENCIA', 3, 'Semestral', true, NOW(), NOW()),
    (gen_random_uuid(), 'Programa de Mentoría', 'Ser mentor de alumnos de nuevo ingreso.', 'TRASCENDENCIA', 5, 'Anual', true, NOW(), NOW())
) AS v
WHERE NOT EXISTS (SELECT 1 FROM actividades);
