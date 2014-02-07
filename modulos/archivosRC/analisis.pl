:- dynamic vista/1, nodoDesde/1, nodoHasta/1, sucesor/2.
:- multifile regla/7, regla/5.

/****************************************


analisis(Modulo, Vista, Texto)


*****************************************

aplicación de las reglas de Modulo al fragmento de texto indicado por Texto, visto según Vista.


Se utilizan las siguientes estructuras de datos :

**********  arco(Final, Inicial, Rotulo)   **********************

Final  -  vértice final

Inicial -  vértice inicial

Rotulo -  rótulo del arco . Es una lista con resto variable (Lin).

Estructura de Lin  :  Los elementos instanciados representan atributos del 
fragmento de texto abarcado por el arco.La posición indica el tipo 
de atributo. 



Manejamos 2 formatos de reglas: 
El formato 1 es un nuevo formato extendido (modif. 11 de marzo)
El 2 se mantiene por compatibilidad con el formato anterior en el cual están escritas muchas reglas.
Las reglas con formato 2 funcionan como las del 1 con Condición y Acción = true 

********** 1- regla(HC, Modulo, Nombre, LIZQ, lDer(L1,L2),Condicion,Accion)  *************
********** 2- regla(HC, Modulo, Nombre, LIZQ, lDer(L1,L2))  *************


Representación interna de las reglas



HC - Head Corner - Categoría (lista Lin) que se utiliza como pivot para
 disparar reglas. El Head Corner es en general el símbolo de más a la derecha.
 Puede no serlo si las categorías a la derecha del mismo son terminales para el módulo.


Modulo - nombre del módulo al cual pertenece la regla


Nombre - nombre que identifica externamente a la regla


LIZQ - lado izquierdo. Rótulo (lista Lin) del arco que se agrega si la 
aplicación de la regla es exitosa.


L1 - Lista de elementos a la izquierda del Head Corner. Sus elementos
      son de alguno de los siguientes tipos

   ob(Cats)    elemento obligatorio
   no(Cats)	   esta es una condición que refiere a elementos terminales y que se aplica 
   			   sólo a 	contextos: no existe un arco Cats terminando en Pos. Se avanza una posición. Una categoría de tipo 'no' no puede ser adyacente a una zona.
   nc(Cats,CatsE)Cats es un elemento obligatorio, no comprendido en elementos de CatsE
   op(Cats)    elemento opcional
   el(Cats)    elemento obligatorio que se elimina. Es responsabilidad del usuario no eliminar elementos de los contextos
   			   o el right corner
   ex(Lista,N) saltear hasta N objetos de texto,parando si aparece un elemento de L
   exR(Lista,N,M) saltear un minimo de M  y hasta N objetos de texto, parando si aparece un elemento de L
   				tanto en ex como en exR se toma el espacio mas largo
   fin         posición del vértice final del arco a agregar 
               si la regla es exitosa
   inicio      posición del vértice inicial del arco a agregar 
               si la regla es exitosa            
 
Cats es una lista incompleta de atributos (Lin).
CatsE es una lista de listas incompletas

Los elementos anteriores a Fin y posteriores a Inicio pertencen a 
los contextos derecho e izquierdo respectivamente.




L2 - Eventuales elementos opcionales u obligatorios (simples) a la derecha del Right Corner.

 *** ojo !! y si permitimos no(Cat) y zonas a la derecha del head ? Parece razonable tratándose de categorías terminales.


Condición/Accion - predicados Prolog que deben ser definidos al definir la regla



*/




/*
El intérprete opera sobre el grafo de texto agregando y eventualmente borrando aristas
- Recorre el grafo de izquierda a derecha.
- Para cada arco que termina en una posición p analiza si la categoría del arco es disparador de alguna regla
  (reglasPosibles). En caso afirmativo dispara las reglas asociadas. Esto se hace de dos modos distintos:
	- grupo de reglas con prioridades (analisisPr)
	  Se disparan las reglas en el orden indicado. Si una es exitosa no se disparan las siguientes.
	- grupo de reglas sin prioridades (analisis. Se disparan todas las reglas, en el orden indicado.

El intérprete (ya sea con prioridades o no) analiza sólo las reglas del módulo que aparece en la invocación.
Dentro de cada invocación, el modo de trabajo es homogéneo (todas las reglas con prioridad o ninguna con prioridad).

A su vez, en cada una de las modalidades, el intérprete maneja un parámetro (Vista) que indica cómo "ve" el grafo. 
La Vista es también única para cada corrida. 
Las posibilidades son:  
	- vista completa (ap): el nodo siguiente es el nodo actual + 1
	- vista por categorías máximas (ot) : el nodo siguiente a un nodo dado es el extremo final 
										del arco máximo que sale de	ese nodo. 
									 


Podría pensarse en que de hecho hay 4 intérpretes, resultantes de combinar las opciones de con o sin prioridad
con las 2 modalidades de Vista. En cada ejecución del intérprete opera sólo uno de éstos.
*/


/*******************************************************************************

               analisis(Modulo,Vista,Posiciones)

Modulo - nombre del modulo (conjunto de reglas) que se ejecuta
Vista - visión del grafo. Puede ser ot o ap
Posiciones - pos(I,F) - I es la posición inicial desde la que se empiezan a aplicar reglas y F la final
               
********************************************************************************/

% Vista se maneja como un argumento global

analisis(Modulo,Vista,pos(I,F)) :-
	  retract(nodoDesde(_)),
	  retract(nodoHasta(_)),
	  asserta(nodoDesde(I)),
	  asserta(nodoHasta(F)),
      (vista(V) ->
	   retract(vista(V));
	   true
	  ),
	  assert(vista(Vista)),
	  analiza(Modulo,F,I).

% analiza se ocupa de scannear el grafo, invocando a reglasPosición que disparará las reglas
% nodoSig da el próximo nodo según la vista que se haya seleccionado

analiza(Modulo,F,I) :-
      I < F,!, 
      nodoSig(I,I1),
	  reglasPosicion(Modulo,I1),
      analiza(Modulo,F,I1).
analiza(_,_,_).

/*******************************************************************************

               reglasPosicion(Modulo,P)
               
********************************************************************************/   

/* 
Para todos los arcos que finalizan en la posición P
se intenta aplicar todas las reglas que tengan la categoría
del arco como Head Corner.
Siempre exitoso
*/   
                                                          
% arcoInc permite obtener arcos incidentes en P (que salen de P, en realidad); depende de la vista
% se copia la categoría para evitar unificaciones indeseadas

reglasPosicion(Modulo,P) :-
   (  arcoInc(P,J,Rotulo),
      copy_term(Rotulo,Rotulo1),
      reglasPosibles(Rotulo1,Modulo,LReglas),
      aplicarReglas(Rotulo1,P,J,Modulo,LReglas), % modif 18/7
      fail
   ;  true
   ).

% aplicaRegla es exitoso si la regla es aplicable, en caso contrario falla; por eso se prevee una cláusula alternativa
% formatoRegla unifica los 2 formatos de regla (5 y 7 args.)
% modif 18/7, se agrego un argumento y se invoca a reglas posibles nuevamente para probar con reglas anteriores en la lista 
% cuando hay un cambio por aplicacion exitosa de una regla

aplicarReglas(_,_,_,_,[]) :- !.
aplicarReglas(Rot,P,J,Modulo,[R|Rs]) :-
      formatoRegla(_,Modulo,R,Izq,Der,Cond,Acc),
	  aplicarRegla(Modulo,pos(P,J),Izq,Der,Cond,Acc),
	  reglasPosibles(Rot,Modulo,LReglas),
      aplicarReglas(Rot,P,J,Modulo,LReglas).
aplicarReglas(Rot,P,J,Modulo,[_|Rs]) :-
      aplicarReglas(Rot,P,J,Modulo,Rs).


formatoRegla(H,Modulo,R,Izq,Der,Cond,Acc):-
		regla(H,Modulo,R,Izq,Der,Cond,Acc),!.
formatoRegla(H,Modulo,R,Izq,Der,true,true):-
		regla(H,Modulo,R,Izq,Der).

% def. provisoria para evitar error de no existencia
regla(a,a,a,a,a,a,a).

regla(a,a,a,a,a).



/*******************************************************************************

               aplicarRegla(pos(P,J),Izq,lDer(L1,L2),Cond,Acc)
               
********************************************************************************/

/*
La aplicación exitosa de una regla implica que se cumplen las condiciones respecto a secuencias
de categorías expresadas por L1 y L2 y las condiciones expresadas sobre valores de atributos expresadas en Cond.
En este caso se realizan las acciones expresadas en Acc y se agrega un arco al grafo de texto. 
En caso contrario, la aplicacion de la regla falla. 
 
La aplicación de la regla también falla si el arco a agregar ya existe *** revisar esto !!

*/


aplicarRegla(Modulo,pos(P,_),Izq,lDer(L1,L2),Cond,Acc) :-
   	  procesoRcIzq(L1,limites(Pf,Pi),P,L1Acc,[]),
      procesoRcDer(L2,limites(Pf,Pi),P,L2Acc,L1Acc),
	  Cond,
	  Acc,
      arcoNuevo(Pf,Pi,Izq),
	  procesoAcc(L2Acc),
      copy_term(Izq,Rotulo),
      reglasPosiblesSinFalla(Rotulo,Modulo,L),
      aplicarReglas(Rotulo,Pf,Pi,Modulo,L).  % modif 18/7


% aplicarRegla no debe fallar cuando efectivamente se agrega un arco. Como reglasPosibles puede fallar,
% se encapsula en un predicado que nunca falla (aplicarReglas con lista de reglas vacia es tambien exitoso).

reglasPosiblesSinFalla(R,M,L) :-
	  reglasPosibles(R,M,L),!.
reglasPosiblesSinFalla(_,_,[]).


% proceso de acciones que se acumularon durante el procesamiento de la regla.


procesoAcc([]).
procesoAcc([X|Xs]) :-
	X,
	procesoAcc(Xs).
	

% no pueden haber arcos con = nodo inicial y final (*** revisar esto !!).
arcoNuevo(P,P,_) :- !,fail.

arcoNuevo(Pf,Pi,Cat) :- 
	  arco(Pf,Pi,Cat),
      !, fail.
      
arcoNuevo(Pf,Pi,Cat) :- 
	  asserta(arco(Pf,Pi,Cat)),
	  asserta((sucesor(Pi,Pf):-!)).

     



/*******************************************************************************

               analisisPr(Modulo, Posición)
               
********************************************************************************/

/* 
recorrida secuencial del grafo de texto, tratando de disparar
las reglas del Módulo para arcos que inciden en cada posición
determinista, según prioridad dada por orden en reglasPosibles, siempre exitoso.
*/ 

% sólo se comentan las diferencias con analisis y analiza

analisisPr(Modulo,Vista,pos(I,F)) :-
	  retract(nodoDesde(_)),
	  retract(nodoHasta(_)),
	  asserta(nodoDesde(I)),
	  asserta(nodoHasta(F)),
	  (vista(V) ->
	   retract(vista(V));
	   true
	  ),
	  assert(vista(Vista)),
      analizaPr(Modulo,F,I).
   
analizaPr(Modulo,F,I) :-
      I < F,!, 
      nodoSig(I,I1),
	  reglasPosicionPr(Modulo,I1),
	  analizaPr(Modulo,F,I1).
analizaPr(_,_,_).







/*******************************************************************************

               reglasPosicionPr(Modulo,P)
               
********************************************************************************/   

/* 
Para todos los arcos que finalizan en la posición P
se aplican las reglas que tengan la categoría
del arco como Head Corner, en orden y parando cuando hubo éxito.
Siempre exitoso
*/   
                                                          


reglasPosicionPr(Modulo,P) :-
   (  arcoInc(P,J,Rotulo),
      copy_term(Rotulo,Rotulo1),
      reglasPosiblesPr(Rotulo1,Modulo,LReglas),
      aplicarReglasPr(P,J,Modulo,LReglas),
      fail
   ;  true
   ).
      
aplicarReglasPr(_,_,_,[]) :- !.
aplicarReglasPr(P,J,Modulo,[R|_]) :-
      formatoRegla(_,Modulo,R,Izq,Der,Cond,Acc),
      aplicarReglaPr(Modulo,pos(P,J),Izq,Der,Cond,Acc),!.
     
aplicarReglasPr(P,J,Modulo,[R|Rs]) :-
      aplicarReglasPr(P,J,Modulo,Rs).
	  

aplicarReglaPr(_,pos(P,_),Izq,lDer(L1,L2),Cond,Acc) :-
	  vista(ot),!,
      procesoRcIzq(L1,limites(Pf,Pi),P,L1Acc,[]),
      procesoRcDer(L2,limites(Pf,Pi),P,L2Acc,L1Acc),
	  Cond,
   	  Acc,
      arcoNuevo(Pf,Pi,Izq),
	  procesoAcc(L2Acc).


aplicarReglaPr(_,pos(P,_),Izq,lDer(L1,L2),Cond,Acc) :-
	  vista(ap),
	  repeat,
      procesoRcIzq(L1,limites(Pf,Pi),P,L1Acc,[]),
      procesoRcDer(L2,limites(Pf,Pi),P,L2Acc,L1Acc),
	  Cond,
   	  Acc,
      arcoNuevo(Pf,Pi,Izq),
	  procesoAcc(L2Acc),
	  fail.

      	  
/*   
regla(HC, Modulo, Nombre, LIZQ, lDer(L1,L2),Cond,Acc)
*/  

/*

El procesamiento de una regla involucra una sucesión de etapas :
- procesamiento del contexto derecho
- procesamiento de la parte reescritura
- procesamiento del contexto izquierdo
- evaluación de condiciones
- procesamiento de acciones
- agregado de un arco al grafo de texto

Cualquiera de las etapas de procesamiento puede fallar,
en ese caso, no se agrega un nuevo arco y el predicado
aplicarRegla/3 falla.
El procesamiento no se realiza explícitamente siguiendo estas etapas,
sino que diferenciando entre elementos a la izquierda del Head Corner
(L1) y  elementos a la derecha del mismo (L2).

*/
 


/*******************************************************************************

              procesoRcIzq(L,limites(Pf,Pi),P,L1Acc,L0Acc) 
               
********************************************************************************/
/*

procesamiento de L a partir de la posición P, recorriendo el grafo de texto.
Se realiza por induccion estructural en el elemento (o par de elementos) de L que
se está analizando.

Pi    - Vértice inicial del arco a crear
Pf    - Vértice final  del arco a crear. 
P     - Posición final del Head Corner en el grafo de texto. 
L1Acc - Lista resultante de acciones
L0Acc - Lista inicial de acciones

*/


procesoRcIzq([],Lims,_,LAcc,LAcc) :- !.                 % fin exitoso

procesoRcIzq([ob(Cats)|R],Lims,P,L1,L0) :-          % ob(...) es requerida
      !,
      unif(P,Cats,J),
      procesoRcIzq(R,Lims,J,L1,L0).

procesoRcIzq([el(Cats)|R],Lims,P,L1,L0) :-          % el(...) es requerida y se elimina el arco al final
      !,
      unif(P,Cats,J),
      procesoRcIzq(R,Lims,J,L1,[retract(arco(P,J,Cats))|L0]).

procesoRcIzq([nc(Cat,CatsE)|R],Lims,P,L1,L0) :-          % agregado 9/10, NoContenido
      !,
      unif(P,Cat,J),
	  noContenido(P,J,CatsE),
      procesoRcIzq(R,Lims,J,L1,L0).
	  
	  
procesoRcIzq([fin|R],limites(P,Pi),P,L1,L0) :-      % se fija fin del nuevo arco
      !,
      procesoRcIzq(R,limites(P,Pi),P,L1,L0).

procesoRcIzq([inicio|R],limites(Pf,P),P,L1,L0) :-   % se fija inicio del nuevo arco
      !,
      procesoRcIzq(R,limites(Pf,P),P,L1,L0).
      
procesoRcIzq([op(Cats)|R],Lim,P,L1,L0) :-           % si el elemento op(...) no se encuentra
      !,                                      % se omite su procesamiento
   (  unif(P,Cats,J) ->
      procesoRcIzq(R,Lim,J,L1,L0)
   ;  procesoRcIzq(R,Lim,P,L1,L0)
   ).

procesoRcIzq([ex(LCats,N)|R],Lim,P,L1,L0) :-
      !,
      procesoE(LCats,N,P,Pres),
      procesoRcIzq(R,Lim,Pres,L1,L0).

% agregado 19/7 - zona de exclusion con rango exR(LCats,N,M) existe un espacio de largo <= N y
% >= M donde no hay arcos con Cat en LCats : se toma el mas largo de estos espacios.

procesoRcIzq([exR(LCats,N,M)|R],Lim,P,L1,L0) :-
      !,
      procesoER(LCats,N,M,P,Pres),
      procesoRcIzq(R,Lim,Pres,L1,L0).

% modif 27/02 - agregado de negación de categorías básicas
procesoRcIzq([no(Cats)|R],Lim,P,L1,L0) :-
      !,
      \+ (arco(P,_,Cats)),
	  nodoAnt(P,P1),
      procesoRcIzq(R,Lim,P1,L1,L0).
      
% procesamiento de una zona, hacia la izquierda
% LCats - Lista de Categorías excluidas
% N - tamaño máximo de la zona
% Pac - Posición corriente de chequeo en la zona
% Pf - Posición final de la zona

procesoE(LCats,N,Pac,Pac) :-
      N > 0,
      arcoInc(Pac,_,Rotulo),
      unifEnL(LCats,Rotulo),
      !.

procesoE(LCats,N,Pac,Pf) :-
      N > 0,
	  nodoAnt(Pac,P1),
	  nodoDesde(D),
	  P1 >= D,
      N1 is N-(Pac-P1),
      !,
      procesoE(LCats,N1,P1,Pf).
      
procesoE(_,_,Pin,Pin).



% ------------------------

% procesamiento de una zona con Rango, hacia la izquierda
% LCats - Lista de Categorías excluidas
% N - tamaño máximo de la zona
% M - tamanio minimo de la zona
% Pac - Posición corriente de chequeo en la zona
% Pf - Posición final de la zona

procesoER(LCats,N,M,Pac,Pac) :-
      N > 0,	  
      arcoInc(Pac,_,Rotulo),
      unifEnL(LCats,Rotulo),
      !,
	  \+ (M > 0).

procesoER(LCats,N,M,Pac,Pf) :-
      N > 0,
	  nodoAnt(Pac,P1),
	  nodoDesde(D),
	  P1 >= D,
      N1 is N-(Pac-P1),
	  M1 is M-(Pac-P1),
      !,
      procesoER(LCats,N1,M1,P1,Pf).
      
procesoER(_,N,M,Pin,Pin) :-
	  \+ (N <0),
	  \+ (M > 0).


% ------------------------

unifEnL([C|_],R) :-
      copy_term(C,C1),
      copy_term(R,R1),
      C1 = R1,
      !.
      
unifEnL([_|Cs],R) :-
      unifEnL(Cs,R).     
	  

noContenido(F,I,[C|Cs]) :-
	  \+(abarca(C,F,I)),
	  noContenido(F,I,Cs).
noContenido(F,I,[]).

abarca(C,F,I) :-
	  arco(N,M,C),
	  N >= F,
	  M =< I.

   
/*******************************************************************************

                 procesoRcDer(L,Limites,P)
               
********************************************************************************/ 

/*
Procesamiento de elementos a la derecha del Head Corner
Similar a procesoRcIzq, sólo que el desplazamiento es "hacia la derecha"

Dado que se trabaja bajo el supuesto de que las categorías necesarias para
satisfacer una regla ya están calculadas, y el parser trabaja básicamente
de izquierda a derecha, no es correcto incluir en el lado derecho (después
del HC) categorías que figuren en el lado izquierdo de reglas del mismo
módulo.
*/ 


/*******************************************************************************

              procesoRcDer(L,limites(Pf,Pi),P) 
               
********************************************************************************/
/*

procesamiento de L a partir de la posición P, recorriendo el grafo de texto, hacia la derecha.
Se realiza por induccion estructural en el elemento (o par de elementos) de L que
se está analizando.

Pi    - Vértice inicial del arco a crear
Pf    - Vértice final  del arco a crear. 
P     - Posición final del Head Corner en el grafo de texto. 

*/


procesoRcDer([],Lims,_,L,L) :- !.                 % fin exitoso

procesoRcDer([ob(Cats)|R],Lims,P,L1,L0) :-          % ob(...) es requerida
      !,
	  arcoSal(P,J,Cats),
      procesoRcDer(R,Lims,J,L1,L0).

procesoRcDer([el(Cats)|R],Lims,P,L1,L0) :-          % el(...) es requerida y se elimina al final
      !,
	  arcoSal(P,J,Cats),
      procesoRcDer(R,Lims,J,L1,[retract(arco(J,P,Cats))|L0]).

procesoRcDer([nc(Cat,CatsE)|R],Lims,P,L1,L0) :-          % agregado 9/10, NoContenido
      !,
      arcoSal(P,J,Cat),
	  noContenido(J,P,CatsE),
      procesoRcDer(R,Lims,J,L1,L0).
	  
	  
procesoRcDer([fin|R],limites(P,Pi),P,L1,L0) :-      % se fija fin del nuevo arco
      !,
      procesoRcDer(R,limites(P,Pi),P,L1,L0).

procesoRcDer([inicio|R],limites(Pf,P),P,L1,L0) :-   % se fija inicio del nuevo arco
      !,
      procesoRcDer(R,limites(Pf,P),P,L1,L0).
      
procesoRcDer([op(Cats)|R],Lim,P,L1,L0) :-           % si el elemento op(...) no se encuentra
      !,                                      % se omite su procesamiento
   (  arcoSal(P,J,Cats) ->
      procesoRcDer(R,Lim,J,L1,L0)
   ;  procesoRcDer(R,Lim,P,L1,L0)
   ).

procesoRcDer([ex(LCats,N)|R],Lim,P,L1,L0) :-
      !,
      procesoED(LCats,N,P,Pres),
      procesoRcDer(R,Lim,Pres,L1,L0).

% modif 27/02 - agregado de negación de categorías básicas
procesoRcDer([no(Cats)|R],Lim,P,L1,L0) :-
      !,
      \+ (arco(_,P,Cats)),
	  nodoSig(P,P1),
      procesoRcDer(R,Lim,P1,L1,L0).
      
% procesamiento de una zona, hacia la derecha
% LCats - Lista de Categorías excluidas
% N - tamaño máximo de la zona
% Pac - Posición corriente de chequeo en la zona
% Pf - Posición final de la zona

procesoED(LCats,N,Pac,Pac) :-
      N > 0,
      arcoSal(Pac,_,Rotulo),     
      unifEnL(LCats,Rotulo),
      !.

procesoED(LCats,N,Pac,Pf) :-
      N > 0,
	  nodoSig(Pac,P1),
	  nodoHasta(H),
	  P1 < H,
      N1 is N-(P1-Pac),
      !,
      procesoED(LCats,N1,P1,Pf).
      
procesoED(_,_,P,P).



/*******************************************************************************


                  unif(Final,CategoriasRegla,Inicial)

*******************************************************************************/

/*

Unificacion no destructiva entre las categorias de una regla y de un arco incidente
en Final.
Se evita de este modo modificar las partes incompletas de estas estructuras.


*/

/* modif 2412
unif(Posicion,CategoriasRegla,In) :-
   arcoInc(Posicion,In,Cats),
   copy_term(Cats,CatsArco),
   copy_term(CategoriasRegla,CatsR),
   CatsR = CatsArco.                  
fin modif 2412
*/
% sólo 2412
unif(Posicion,Cats,In) :-
   arcoInc(Posicion,In,Cats).
         
%fin sólo 2412 

arcoInc(F,P,C) :-
	vista(ot),!,
	nodoAnt(F,P),
	arco(F,P,C),!.
arcoInc(F,P,C) :-
	vista(ap),
	arco(F,P,C).



arcoSal(I,F,C) :-
	vista(ot),!,
	nodoSig(I,F),
	arco(F,I,C),!.
arcoSal(I,F,C) :-
	vista(ap),
	arco(F,I,C).
   


	

	

nodoSig(I,I1):-
	vista(V),
	nodoSig(V,I,I1).	

nodoSig(ot,I,I1):-
	sucesor(I,I1),!.
	
nodoSig(_,I,I1):-
	nonvar(I),!,
	I1 is I+1.

nodoSig(_,I,I1):-
	nonvar(I1),!,
	I is I1-1.

nodoAnt(I,I1):-
	nodoSig(I1,I).
	
	
	
vista(ap).

nodoDesde(1).
nodoHasta(1).