# Pong-Tutorial

Videojuego sencillo de Pong realizado con libGDX.

Hecho para el tutorial: [Crea tu primer juego con libGDX](http://elblogdejulioxus.noip.me/crea-tu-primer-juego-con-libgdx/)

## Autores

* Julio Martínez Martínez-Checa
* Hans Manuel Grenner Noguerón

## Disponible en Google Play

[https://play.google.com/store/apps/details?id=com.codamasters.pong](https://play.google.com/store/apps/details?id=com.codamasters.pong)


## Capturas de pantalla

![gameplay_pong](http://elblogdejulioxus.noip.me/wp-content/uploads/2015/05/Screenshot_2015-05-10-19-35-21.png)

![menu_pong](http://elblogdejulioxus.noip.me/wp-content/uploads/2015/05/Screenshot_2015-05-10-19-35-10.png)

## Compilación

Prerrequisitos:
Tener java instalado, la variable de entorno JAVA_HOME bien situada y el IDE Eclipse

1. Descargar [LibGDX](http://libgdx.badlogicgames.com/)
2. Crear proyecto nuevo ejecutando el .jar
3. Configurar el proyecto de la siguiente manera:

![configuracion](http://i.imgur.com/y7Nx6fF.png)

En configuración avanzada activar Eclipse:

![avanzada](http://i.imgur.com/xge14Yi.png)

4. Pulsamos en generate y se nos creará el proyecto en la carpeta seleccionada
5. Descargamos el proyecto de nuestro repositorio (usando git clone)
6. Sustituir las carpetas dentro de core, introducir las carpetas android/assets/data, y desktop/data de nuestro proyecto en el proyecto que hemos creado
7. Tener en cuenta que hace falta importar las librerias de TweenEngine (están en nuestro proyecto metidas en core/libs)
8. Hay que tener en cuenta que para compilar debemos alterar el orden haciendo que TweenEngine quede por encima de LibGdx
