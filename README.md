lavinia
=======

Lavinia es un ambiente basado en UIMA para Procesamiento de Lenguaje Natural, desarrollado por el Grupo de Procesamiento de Lenguaje Natural del Instituto de Computación, Facultad de Ingeniería, Universidad de la República en Montevideo, Uruguay

Sus principales características son:

- Es un ambiente Web, por lo que puede ser utilizado sólo con un navegador.
- Utiliza la arquitectura UIMA de IBM como motor de procesamiento y como formato de intercambio de la información entre los diferentes módulos
- Provee mecanismos de fácil uso para el armado de flujos de análisis de textos, incluyendo la capacidad de parametrización y especificación de recursos externos.
- Provee un mecanismo homogéneo y amigable de visualización de las anotaciones sobre los textos analizados, incluyendo anotaciones solapadas.
- Permite exportar los resultados en formato XML, e importar resultados de análisis anteriores
- Cualquier módulo desarrollado para la plataforma UIMA puede incorporarse rápidamente a Lavinia
- Incluye varios componentes ya implementados para realizar tareas comunes de procesamiento de Lenguaje Natural.

Si utiliza Lavinia, por favor cite el siguiente artículo:

Cecilia Techera, Diego Garat, Guillermo Moncecchi "Lavinia: un Entorno para Procesamiento de Lenguaje Natural", Setiembre de 2009 - 7th Brazilian Symposium in Information and Human Language Technology

Módulos ya implementados
========================

- Tokenizador: basado en la suite de herramientas Freeling para PLN. Disponible para español, inglés, catalán e italiano.
- POS Tagger: basado en la suite de herramientas Freeling para PLN. Disponible para español, inglés, catalán e italiano. Incluye reconocimiento de POS Tags, Números, Fechas, Cantidades y Palabras Compuestas.
- Sentence Splitter: separador del texto en oraciones, basado en al suite de herramientas Freeling para PLN. Disponible para español, inglés, catalán e italiano.
- Shallow Parser: análisis superficial de textos, basado en la suite de herramientas Freeling para PLN. Disponible para español, inglés, catalán e italiano.
- Analizador de Reglas Contextuales: basado en un analizador en Prolog del formalismo de Reglas Contextuales, desarrollado por el grupo PLN del InCo. Recibe un conjunto de reglas contextuales, y un texto con etiquetas de POS y devuelve texto marcado, resultado de su aplicación.
