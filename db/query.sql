create database tienda;

use tienda;

CREATE TABLE producto (
    idproducto INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255),
    stock INT,
    precio DECIMAL(10, 2)
);
-- Añadir restricción UNIQUE a la columna descripcion
ALTER TABLE producto
ADD CONSTRAINT unique_descripcion UNIQUE (descripcion);

-- Insertar productos de abarrotes
INSERT INTO producto (descripcion, stock, precio) VALUES
('Arroz', 50, 2.99),
('Frijoles', 30, 1.99),
('Aceite de cocina', 20, 4.50),
('Sal', 40, 0.99),
('Azúcar', 25, 3.25);

describe producto;
select * from producto;


select stock from producto where idproducto = 1;