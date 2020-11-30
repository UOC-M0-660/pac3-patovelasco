# PARTE TEORICA

### Lifecycle

#### Explica el ciclo de vida de una Activity.

##### ¿Por qué vinculamos las tareas de red a los componentes UI de la aplicación?
Para poder trabajar con multitareas ya que esto permite que una aplicación realice acciones en segundo plano, mientras el usuario puede seguir utilizando la aplicación, en tareas de larga duración nos permitirá no bloquear la interfaz de usuario de la aplicación.

##### ¿Qué pasaría si intentamos actualizar la recyclerview con nuevos streams después de que el usuario haya cerrado la aplicación?
No es posible puesto que al cerrar la aplicación se invoca al método onDestroy lo que ocasiona liberar recursos, quitando de la vista los elementos, por lo que se produciría un error.

##### Describe brevemente los principales estados del ciclo de vida de una Activity.
* **onCreate():** se llama cuando se lanza la aplicación y donde se le asignará la vista que se mostrará mediante el método setContentView().
* **onStart():** es llamado después de onCreate() y es el momento en el que la actividad está a punto de volverse visible al usuario.
* **onResume():** es llamado después de onStart(), en este punto la actividad por fin está en primer plano y se vuelve visible al usuario.
* **onPause():** es llamado cuando la actividad pierde el foco o deja de estar en primer plano. 
* **onStop():**  es llamado cuando la actividad deja de ser visible, puede ser causado porque la actividad ha sido destruida, una nueva actividad está siendo lanzada o una actividad existente vuelve a estar en primer plano.
* **onRestart():** es llamado después de onStop() si es que el usuario eligió poner nuevamente la actividad en primer plano.
* **onDestroy():** es llamado cuando el usuario elige dar por finalizada su interacción con la actividad. Este método permite liberar recursos que se han estado utilizando por la actividad.


---

### Paginación 

#### Explica el uso de paginación en la API de Twitch.

##### ¿Qué ventajas ofrece la paginación a la aplicación?
* Mejora el rendimiento de la app, puesto que la velocidad de carga será más eficiente al limitar en un rango los datos a mostrar
* La carga de información se realiza bajo demanda lo cual permite cargar información de acuerdo a la necesidad

##### ¿Qué problemas puede tener la aplicación si no se utiliza paginación?
* Lentitud puesto que si los datos a mostrar son extensos, estos se tardarán en presentar debido a la cantidad de registros
* Sobrecarga de recursos, puesto que el rendimiento se verá deteriorado lo cual hará que se utilicen mas recursos de los adecuados

##### Lista algunos ejemplos de aplicaciones que usan paginación.
* **Con Paginación:** Apps de ventas, busquedas
* **Con Scroll:** Apps de redes sociales como Facebook, Twitter, Instagram
