## Paso 1

Primero ajuste el prompts a mi estilo pero manteniendo toda la información relevante y como pedia el ejercicio. En este caso use una IA como Gemini para que me diera una base y luego yo le modifique las cosas que yo veia falta.


## Paso 2

Fui al Copilot y con el modo Agent puesto escribi este mensaje "/create-implemention-plan.prompt.md siguiendo las estructuras que ya tengo hechas hazme la implementacion de MongoDb sin un backend de por medio. IMPORTANTE lo que hay que guardar es el record y la fecha"

Esto lo que hace es agarrar el archivo en cuestion que en este caso se designa con "/" y le pedi que me lo hiciera sin un backend porque si no iba a aser mas trabajo y para lo que necesitabamos no hacia falta.


## Paso 3

Confirmamos que ha funcionado buscando una carpeta llamada plan y dentro de esta que se haya creado un archivo llamado feature-database-1.md con el contenido que se pide en la plantilla.

<img width="237" height="76" alt="image" src="https://github.com/user-attachments/assets/450a2ba2-c569-4956-b113-f5c89242da42" />


## Paso 4

Para lo siguiente que tenemos que hacer es darle permisos a la IA para poder fuchicar en nuestro GitHub. Para esto primero fui al chat de Copilot y le di a la llave +Add More Tools.

<img width="378" height="116" alt="image" src="https://github.com/user-attachments/assets/19ebf76f-93c4-44cb-9b8c-d20a9d49d4af" />

Lo siguiente fue ir a mi GitHub, entre en Settings y busque Developer Settings. Dentro de esta busque Personal Access Tokens y seleccione Tokens (classic). Le di a Generate new token y le puse un nombre, una expiracion y los permisos que necesitaba. En este caso solo necesitaba los de repo asi que seleccione todos los de repo.

<img width="1162" height="328" alt="image" src="https://github.com/user-attachments/assets/0a4c5221-5a47-493d-9c89-1d0335c73128" />

Con eso hecho me dio un codigo que copie y pegue aqui
```
{
"servers": {
"github": {
"url": "https://api.githubcopilot.com/mcp/",
"requestInit": {
"headers": {
"Authorization": "Bearer CODIGO"
}
}
}
}
}
```

IMPORTANTE que para que Copilot pueda realizar los Issues necesitamos darle Play primero para Autorizar a la IA a que pueda hacer cambios en nuestro GitHub.

## Paso 5

Ahora que ya tenemos los permisos volvemos al chat de Copilot y le pedimos que realice los Issues en nuestro repositorio Andrea-Mourino/Simon. 

Si no hay ningun error deberia de verse ya en nuestro repo lo Issues creados correctamente.

<img width="1517" height="769" alt="image" src="https://github.com/user-attachments/assets/ea3dfa3a-bbe3-4dde-89a3-95c08acdc1d4" />



