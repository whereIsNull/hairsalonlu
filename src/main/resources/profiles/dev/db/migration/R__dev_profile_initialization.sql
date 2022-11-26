insert into menuitem(id, parent, menu_key, menu_value, menu_service, grid_def, expanded)
values(0, -1, 'master', 'System', null, null, 1);

insert into menuitem(id, parent, menu_key, menu_value, menu_service, grid_def, expanded, image)
values(2, 0, 'master.products', 'Servicios', 'productService', 'productGridDef', 0, 'category_16x16.png');

insert into menuitem(id, parent, menu_key, menu_value, menu_service, grid_def, expanded, image)
values(4, 0, 'master.invoices', 'Facturas', 'invoiceService', 'invoiceGridDef', 0, 'invoice_16x16.png');

INSERT INTO `hairsalonlu`.`category`(`id`, `category`)
values
(1, 'Pedicura'),
(2, 'Manos'),
(3, 'Depilación');

INSERT INTO `hairsalonlu`.`product`(`id`,`category`,`price`,`product_name`)
VALUES
(1, 'Pedicura', 7.00, 'Pedicura uñas'),
(2, 'Pedicura', 12.00, 'Pedicura+esmaltado normal'),
(3, 'Pedicura', 15.00, 'Pedicura+esmaltado semipermanente'),
(4, 'Pedicura', 20.00, 'Pedicura completa+esmaltado normal'),
(5, 'Pedicura', 25.00, 'Pedicura completa+semipermanente'),
(6, 'Pedicura', 27.00, 'Pedicura spa+semipermanente'),
(7, 'Manos', 7.00, 'Manicura'),
(8, 'Manos', 12.00, 'Manicura+esmaltado normal'),
(9, 'Manos', 15.00, 'Manicura semipermanente'),
(10, 'Manos', 18.00, 'Manicura semipermanente+decoración'),
(11, 'Manos', 18.00, 'Manicura+nivelación+esmaltado (1 color)'),
(12, 'Manos', 20.00, 'Manicura+nivelacion+esmaltado (decoración)'),
(13, 'Manos', 92.00, '1º puesta acrílico - a partir de'),
(14, 'Manos', 42.00, '1º puesta acrygel a partir de'),
(15, 'Manos', 25.00, 'Relleno acrílico a partir de'),
(16, 'Manos', 25.00, 'Relleno acrygel a partir de'),
(17, 'Depilación', 6.00, 'Cejas'),
(18, 'Depilación', 3.00, 'Labio'),
(19, 'Depilación', 12.00, 'Facial completo'),
(20, 'Depilación', 8.00, 'Axilas'),
(21, 'Depilación', 10.00, 'Brazos'),
(22, 'Depilación', 15.00, 'Abdomen y pecho'),
(23, 'Depilación', 6.00, 'Hombros'),
(24, 'Depilación', 15.00, 'Espalda'),
(25, 'Depilación', 16.00, 'Media pierna'),
(26, 'Depilación', 26.00, 'Pierna entera'),
(27, 'Depilación', 8.00, 'Ingle'),
(28, 'Depilación', 16.00, 'Ingle integral');


-- insert into customer(id, firstname, lastname, address, email)
-- values(0, 'John ', 'Doe', 'Happiness street 1234', 'john.doe@skynet.com');
