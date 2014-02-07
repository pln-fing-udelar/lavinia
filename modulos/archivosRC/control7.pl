
:- dynamic reglasPosibles/3, regla/5, regla/7.
:- [ 
        klectura,
        kanalisis,
        condiciones,
        convert7,
        despliegueXML
   ].



inicio:-

        write('Ingrese el archivo de REGLAS: '),
        read(ArchReglas),
        cargar_reglas(ArchReglas),
        writeln('[Reglas OK]'), nl,
        write('Ingrese el archivo tagueado: '),
        read(ArchEnt),
        cargar(ArchEnt),
        writeln('[Entrada OK]'), nl,
        findall(Mod,regla(_,Mod,_,_,_,_,_),Mods2),sort(Mods2,Mods),
        write('Ingrese lista de módulos a aplicar '), write(Mods), write(':'),
        read(ListaMods),nl,
        aplicModulos(ListaMods),        
        findall(Et,regla(_,_,_,[Et|_],_,_,_),Ets2),sort(Ets2,Ets),
        nl, nl, write('Ingrese lista de etiquetas a visualizar '), write(Ets), write(':'),
        read(ListaEts),nl,
        write('Ingrese archivo de salida: '),
        read(ArchSal),nl,
        ultimoNodo(N),
        despliegueXML(ListaEts,ArchSal,pos(1,N)).
        
print_texto(N,M):-
        texto(K,_,P), 
        K=<M, 
        K>N, 
        write(P), 
        write(' '), 
        fail. 
print_texto(_,_).

borra :-
      abolish(arco/3), 
      
      abolish(texto/3), 
      abolish(texto/4),
      abolish(sucesor/2),
      asserta(sucesor(0,1)),
    /*
      retract(ultimoNodo(_)),
      assert(ultimoNodo(1)),    
    */
      abolish(ultimoNodo/1),
      assert(ultimoNodo(1)).
      

/*************************************************************************************/

cargar(FT) :-
        %-------- CARGAR EL GRAFO DE TEXO
        ignore(borra),
        cargarGrafo(FT).


aplicModulo(Modulo) :-
        modoAplic(Modulo,Modo),
        nl, write('************************** '), write(Modulo), write('-'), write(Modo), write('**************************'), nl,
        write(Modulo), nl,        
        aplicModulo(Modulo,Modo).
        
aplicModulo(Modulo,normal):-!,
        ultimoNodo(N),
        analisis(Modulo,ot,pos(1,N)).

%aplicModulo(Modulo,preferencia):-!,
%       ultimoNodo(N),
%       analisisPr(Modulo,ot,pos(1,N)).


%modoAplic(Modulo,preferencia):-
%       reglasPosiblesPr(_,Modulo,_),
%       !.
modoAplic(_,normal).

        
aplicModulos([M|Ms]):-
        aplicModulo(M),
        !,
        aplicModulos(Ms).
aplicModulos([]).
