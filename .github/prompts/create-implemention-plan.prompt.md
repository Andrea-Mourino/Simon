---
agente: 'agente'
descripción: 'Crear un nuevo archivo de plan para implementar funcionalidades nuevas, mejorar código existente o actualizar paquetes, diseño, arquitectura o infraestructura.'
herramientas: ['changes', 'search/codebase', 'edit/editFiles', 'extensions', 'fetch', 'githubRepo', 'openSimpleBrowser', 'problems', 'runTasks', 'search', 'search/searchResults', 'runCommands/terminalLastCommand', 'runCommands/terminalSelection', 'testFailure', 'usages', 'vscodeAPI']
---

# 🐾 Crear Plan de Implementación

## 🧸 Directiva Principal

Tu misión es crear un archivo nuevo con un plan paso a paso para `${input:PlanPurpose}`. Tu respuesta debe ser clara, predecible y estar súper organizada, para que otros sistemas de IA o personas puedan usarla fácilmente.
Escribe todo el plan en **Español**.

## 🌈 Contexto de Ejecución

Este mensaje está hecho para que lo lean y lo sigan otros ayudantes digitales o sistemas automáticos. Todas las instrucciones deben tomarse al pie de la letra y ejecutarse de forma ordenada, sin necesidad de que una persona intervenga para explicar nada.

## 📌 Requisitos Básicos

- Los planes que crees deben poder llevarse a cabo tal cual, ya sea por otros ayudantes o por personas.
- Usa un lenguaje claro y directo, sin dejar lugar a dudas.
- Organiza todo el contenido para que sea fácil de analizar y procesar automáticamente.
- Asegúrate de que el plan se entienda por sí solo, sin necesidad de buscar información en otro lado.

## 🧩 Estructura del Plan

Los planes deben dividirse en **etapas pequeñas e independientes**, cada una con tareas concretas que se puedan hacer. Cada etapa debe poder ejecutarse por separado, a menos que se indique explícitamente que necesita de otra.

## 🏗️ Arquitectura de las Etapas

- Cada etapa debe tener una forma clara de saber cuándo está terminada.
- Las tareas dentro de una etapa se pueden hacer al mismo tiempo, a menos que se diga lo contrario.
- Todas las tareas deben incluir detalles específicos: rutas de archivos exactas, nombres de funciones, etc.
- Ninguna tarea debe requerir que una persona tenga que adivinar o tomar una decisión sobre cómo hacerla.

## 🤖 Estándares Optimizados para IA

- Sé explícito y evita cualquier frase que pueda tener más de un significado.
- Organiza la información en formatos fáciles de leer para las máquinas (como tablas y listas).
- Incluye referencias exactas: rutas de archivos, números de línea, nombres de código, etc.
- Define con claridad todas las variables, configuraciones y valores que se vayan a usar.
- Da todo el contexto necesario dentro de la descripción de cada tarea.
- Usa prefijos estandarizados para identificar cosas (como `REQ-`, `TASK-`, etc.).
- Incluye chequeos o pruebas que se puedan verificar de forma automática.

## 📁 Especificaciones del Archivo Final

- Guarda los archivos del plan dentro de la carpeta `/plan/`.
- Sigue esta regla para nombrarlos: `[proposito]-[componente]-[version].md`.
- Prefijos para el propósito: `upgrade|refactor|feature|data|infrastructure|process|architecture|design`.
- **Ejemplo**: `upgrade-system-command-4.md`, `feature-auth-module-1.md`.
- El archivo debe estar en formato Markdown válido y comenzar con un *front matter* adecuado.

## 📋 Plantilla Obligatoria (Estructura)

Todos los planes deben seguir **esta plantilla al pie de la letra**. Cada sección es obligatoria y hay que llenarla con contenido específico y accionable. Los agentes de IA deben verificar que se cumpla la plantilla antes de ejecutar nada.

## ✅ Reglas para Validar la Plantilla

- Todos los campos del *front matter* deben estar presentes y bien escritos.
- Todos los títulos de sección deben coincidir exactamente (respetando mayúsculas y minúsculas).
- Todos los prefijos de identificación deben seguir el formato indicado.
- Las tablas deben tener todas las columnas que se piden.
- ¡No debe quedar ningún texto de relleno o ejemplo en el plan final!

## 🚦 Estado

El estado del plan debe definirse claramente en el *front matter* y debe mostrar su situación actual. El estado puede ser uno de los siguientes (con su colorcito): `Completado` 🟢, `En progreso` 🟡, `Planificado` 🔵, `Obsoleto` 🔴 o `En espera` 🟠. También debe aparecer como una insignia (badge) en la sección de introducción.

---

# 🚀 Plan de Desarrollo: [Nombre del Proyecto]

![Estado: PLANIFICANDO](https://img.shields.io/badge/estado-🧭_PLANIFICANDO-ffaa00)
![Prioridad: ALTA](https://img.shields.io/badge/prioridad-🚨_ALTA-red)
![Esfuerzo: MEDIO](https://img.shields.io/badge/esfuerzo-⚖️_MEDIO-yellow)

**Versión**: 1.0  
**Última actualización**: [Fecha]  
**Creador/Responsable**: [Tu nombre o alias] ✨

## 🎯 Objetivo Principal
[Describe en 1-2 frases QUÉ vas a construir y POR QUÉ mola tanto]

**Ejemplo**: "Voy a hacer una app web para llevar mis gastos personales, que me ayude a ver en qué me gasto la paga y a planificar mis ahorros de forma sencilla y bonita."

## 🧐 Contexto y Motivación

### ¿Por qué me apetece hacer este proyecto?
- [ ] **Necesidad personal**: [Explica tu necesidad o tu "dolor"]
- [ ] **Aprendizaje**: [Qué tecnologías o conceptos nuevos quieres explorar]
- [ ] **Portafolio**: [Si lo quieres enseñar para demostrar lo que sabes]

### ¿Qué problema soluciona?
1. El rollo actual: [Describe la situación que quieres mejorar]
2. La solución chula: [Cómo este proyecto lo hace todo mejor]

## 🗺️ Alcance (Qué SÍ y qué NO)

### ✅ SÍ va a incluir
- [ ] Funcionalidad 1: [Descripción breve]
- [ ] Funcionalidad 2: [Descripción breve]
- [ ] Funcionalidad 3: [Descripción breve]

### ❌ NO va a incluir (por ahora, luego ya veremos)
- [ ] Algo muy complejo que dejamos para más adelante
- [ ] Una integración que no es imprescindible para empezar
- [ ] Optimizaciones que podemos hacer en una futura versión

## 🛠️ Kit de Herramientas (Stack Tecnológico)

| Categoría | Tecnología Elegida | ¿Por qué esta? |
|-----------|-------------------|----------------|
| Frontend | React / Vue / Svelte | [La razón de tu elección] |
| Backend | Node.js / Python / Sin backend | [Por qué te decantas por esta] |
| Base de datos | SQLite / PostgreSQL / Firebase | [Lo que mola para tu caso] |
| Estilos | Tailwind / CSS Modules | [Tu preferencia] |
| Dónde vivirà | Vercel / Netlify / GitHub Pages | [Por tema coste/facilidad] |

## 📅 Plan de Acción (Pasito a Pasito)

### Fase 1: Prototipo que Funcione (Semana 1)
**Meta**: Tener algo mínimo pero que se pueda usar de punta a punta.

| Tarea | Estado | Notas |
|-------|--------|-------|
| Preparar el proyecto | ✅ | Usar vite/create-react-app |
| Diseñar la estructura base | 🔄 | Definir los componentes principales |
| Hacer el flujo básico | ⏳ | Un CRUD simple pero funcional |
| Primer despliegue | | Subirlo a su casita en internet |

### Fase 2: Funcionalidades Clave (Semana 2-3)
**Meta**: Implementar las cosas más importantes que harán útil el proyecto.

| Tarea | Prioridad | Tiempo Estimado |
|-------|-----------|-----------------|
| [ ] Funcionalidad principal 1 | Alta | 2 días |
| [ ] Funcionalidad principal 2 | Alta | 1.5 días |
| [ ] Sistema para guardar datos | Media | 1 día |
| [ ] Mejorar la interfaz | Baja | 2 días |

### Fase 3: Los Toques Finales (Semana 4)
**Meta**: Que quede mono, estable y listo para enseñar.

| Tarea | Descripción | Necesita de... |
|-------|-------------|----------------|
| [ ] Diseño responsive | Que se vea bien en el móvil | Fase 2 |
| [ ] Manejo de errores | Dar feedback al usuario cuando algo falle | Fase 2 |
| [ ] Optimizaciones | Mejorar la velocidad un poquito | Fase 2 |
| [ ] Documentación | Hacer un README guay y poner comentarios en el código | Todo lo anterior |

## 📊 Seguimiento del Progreso

### Tu Tablero Kanban Personal
*(Aquí puedes llevar el control de tus tareas)*