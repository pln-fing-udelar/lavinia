

:- dynamic ultimoNodo/2, arco/3, texto/3.

/**********    Creación del grafo de texto  ************************

cargarGrafo(NombreArchivo)


NombreArchivo  - archivo de texto etiquetado por FreeLing
               - 1 elemento por línea, con 3 items (separados por espacio) :
                     - palabra
                     - lema
                     - tag (secuencia de letras mayúsculas)

El grafo que se obtiene como resultado queda en el ambiente 
como predicado arco/3.

El texto original (sin tags) queda en el ambiente 
como predicado texto/3.

********************************************************************/





primerNodo(1).


cargarGrafo(NombreArchivo) :-
      open(NombreArchivo,read,S,[eof_action(eof_code)]),
      primerNodo(N),
      (retract(ultimoNodo(_));true),!,
      assert(ultimoNodo(N)),
      assert(arco(1,0,[limPhr, '.', '.',inicial])),     %este es para "análisis"
      lectura(S),
      close(S).
      
      
lectura(S) :-          % se utiliza read_words, OKeefe, pag.331
      repeat,
         read_words(S,Words,W1),
         procesoDatos(Words,W1),
         Words == -1,
         !.
    
      
      
read_words(S,Words,W1) :-
      get0(S,C), 
      read_words(S,C,Words,W1).
      
      
read_words(_S,-1,-1,_) :- !.     % fin de archivo

read_words(_S,10,[],[]) :- !.     % fin de l?ea
      
read_words(_S,13,[],[]) :- !.  % CR, separa lineas
 
read_words(S,32,Words,W1) :-     % espacio, separa palabras
           !,
           read_words(S,Words,W1).


read_words(S,C,[Word|Words],[W|Ws]) :-  % C no es puntuaci? ni espacio-tab o eof
      read_rest_of_word(S,Chars,Chars1,LeftOver),
      name(Word,[C|Chars]),
      name(W,[C|Chars1]),
      read_words(S,LeftOver,Words,Ws).
            
read_rest_of_word(S,Chars,Chars1,LeftOver) :-
      get0(S,C), 
      read_rest_of_word(S,C,Chars,Chars1,LeftOver).
      
read_rest_of_word(_S,C,[],[],C) :-
      not_other(C),
      !.
      
read_rest_of_word(S,C,[C|Chars],[C|Cs],LeftOver) :-
      %to_lower(C,C1),
      read_rest_of_word(S,Chars,Cs,LeftOver).
      


to_lower(Letter,Lower) :-
   integer(Letter),
   Letter >= "A", Letter =< "Z",
   !,
   Lower is Letter + ("a" - "A").

to_lower(Letter,Letter).  % Letter no es una mayuscula, queda igual
 
 

%%% GRAFO DE TEXTO



% última línea del archivo

procesoDatos(-1,_) :-!.


% líneas "normales" del archivo


/************* GENERO UN ARCO DEL GRAFO *********/

procesoDatos([Pal,Lema,Cat|_],[P|_]) :- 
                     
      retract(ultimoNodo(N)),
      M is N+1,
       
      % armo la etiqueta: [Categoria,Pal,Lema,At1,At2, ...]
      atom_chars(Cat,[Categoria|Atributos]),
      append([Categoria,Pal,Lema],Atributos,Etiqueta),

      % agrego un arco al grafo de texto y al grafo etiquetado
      assert(texto(M,N,P)),  
      assert(arco(M,N,Etiqueta)),
      assert(ultimoNodo(M)), 
      !.

/***********************************************/       
      

     
     
     
not_other(10).
not_other(-1).
not_other(9).
not_other(13).
not_other(32).

punctuation(0'").
punctuation(0',).            % daban errores con horas, fechas, nmeros!!
punctuation(0':).
punctuation(0'.).
punctuation(0';).                         
punctuation(0'?). 
punctuation(0'!).
punctuation(0'().
punctuation(0')).

