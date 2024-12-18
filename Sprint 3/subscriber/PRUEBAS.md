# Demostraciones de funcionamiento entre el cliente y la base de datos

A continuación muestro resultados de las pruebas ejecutadas para verificar que el cliente es seguro. 
A lo largo de las ejecuciones se comprueban los siguientes casos:<br>

## Mal cuerpo de mensaje

Si se intenta enviar un cuerpo que no sea JSON aparecer el siguiente error: <br>

`Datos enviados`<br>
![bad_body](doc_imgs/test-bad-body.png)

`Datos recibidos`<br>
![bad_body](doc_imgs/feed-bad-body.png)

## Cuerpo incorrecto

Si se intenta enviar un cuerpo JSON pero con campos incorrectos: <br>

`Datos enviados`<br>
![wrong_body](doc_imgs/test-wrong-body.png)

`Datos recibidos`<br>
![wrong_body](doc_imgs/feed-wrong-body.png)

## ID incorrecto

Si se intenta enviar un cuerpo correcto pero con un rfid falso: <br>

`Datos enviados`<br>
![fake_id](doc_imgs/test-fake-rfid.png)

`Datos recibidos`<br>
![fake_id](doc_imgs/feed-fake-rfid.png)

## Sala/aula incorrecta

Si se intenta enviar un cuerpo correcto pero con una aula falsa: <br>

`Datos enviados`<br>
![fake_room](doc_imgs/test-fake-room.png)

`Datos recibidos`<br>
![fake_room](doc_imgs/feed-fake-room.png)


## Aula no asignada

Si los datos son reales, pero se comprueba que el rfid no se de alguien con acceso a esa aula en la hora actual: <br>

`Datos enviados`<br>
![cant_enter](doc_imgs/test-cant-enter.png)

`Datos recibidos`<br>
![cant_enter](doc_imgs/feed-cant-enter.png)

## Datos correctos y acceso permitido

Si los datos son reales, pero se comprueba que el rfid no se de alguien con acceso a esa aula en la hora actual: <br>

`Datos enviados`<br>
![correct](doc_imgs/test-all-ok.png)

`Datos recibidos`<br>
![correct](doc_imgs/feed-all-ok.png)

`Demostración`<br>
![correct](doc_imgs/all-ok-demonstration.png)


