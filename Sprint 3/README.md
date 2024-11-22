# Cliente MQTT

Este programa permite conectarse a un endpoint MQTT, usando certificados privados. Escucha los mensajes de el topic que se le indique.

## Ejecución principal

Para inicar el programa, hace falta usar maven.

 1. Primero que nada, hay que ubicarse dentro de la carpeta subscriber: `cd subscriber`
 2. Configurar el [CLIENT_ENDPOINT](./subscriber/src/main/java/iticbcn/subscriber/Main.java)
 3. Insertar los certificados en la [carpeta resources](./subscriber/src/main/resources/) con los nombres:
  `certificate.pem.crt` y `private.pem.key`.
 4. Luego se debe limpiar el proyecto en caso de haber alguna cosa compilada desactualizada: `mvn clean`
 5. Luego instalamos los plugins y dependencias necesarias: `mvn install`
 6. Luego se debe recompilar de nuevo tood: `mvn compile`
 7. Para finalmente ejecutar el programa, se debe ejecutar con: `mvn exec:java`

El programa entonces se quedará escuchando al endpoint, por el momento solo muestra el mensaje en format string crudo.
Futuramente guardará los datos en una tabla de una base de datos

