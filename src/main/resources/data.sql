-- Configuración de empresa (singleton id=1)
INSERT INTO configuracion_empresa (id, nombre_empresa, nombre_representante, telefono, correo, direccion, condiciones_cotizacion, logo_url, fecha_actualizacion)
VALUES (1,
        'Polyline SAC',
        'Arq. Luis Alberto Salas Castro',
        '+51 943 812 536',
        'polylinesac@yahoo.com',
        'Av. Benavides 3008, Lima',
        'Precios vigentes a la fecha de emisión. Validez de 15 días hábiles. Incluye IGV del 18%. Sujeto a disponibilidad de stock.',
        NULL,
        NOW())
ON CONFLICT (id) DO NOTHING;

-- Usuario administrador por defecto
-- Password: admin123 → BCrypt hash
INSERT INTO usuarios (nombre, correo, password, rol, activo, fecha_creacion, fecha_actualizacion)
VALUES ('Administrador',
        'admin@polyline.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lVuy',
        'ADMIN',
        true,
        NOW(),
        NOW())
ON CONFLICT (correo) DO NOTHING;

-- Categorías base (del frontend original)
INSERT INTO categorias (nombre, icono, color, activo) VALUES
('Acabados para Piso',    'fa-layer-group',  '#C8956C', true),
('Acabados para Pared',   'fa-paint-roller', '#8BAA7E', true),
('Griferías',             'fa-faucet-drip',  '#7EAAB8', true),
('Aparatos Sanitarios',   'fa-bath',         '#B87EA0', true),
('Muebles de Cocina',     'fa-kitchen-set',  '#B8A47E', true),
('Mármol para Cocina',    'fa-table',        '#9E9E9E', true),
('Varios 1',              'fa-puzzle-piece', '#A08BC8', true),
('Varios 2',              'fa-shapes',       '#C8A07E', true)
ON CONFLICT (nombre) DO NOTHING;
