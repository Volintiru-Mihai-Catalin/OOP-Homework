            TEMA POO
                    Volintiru Mihai Catalin 336 CA


    Pentru aceasta tema am ales sa fac diferite pachete care sa contina diferite clase care sa ma
ajute la aceasta tema. Cea mai importanta clasa, unde se observa cel mai bine work flow-ul este
in pachetul "gameplay", GameWorkFlow. Acolo incep efectiv jocul, initializez toate datele care
trebuiesc initializate si parsez multitudinea de comenzi, folosindu-ma de un switch. Tot in acelasi
pachet am si "Commands", o clasa in care printez rezultatul fiecarei comenzi primite in clasa
anterioara.
    Urmatoarele clase importante sunt efectiv obiectele care interactioneaza in joc, si anume
jucatorii, masa si cartile, toate le gasim in "gameobjects". Acestea sunt instantele care
interactioneza intre ele. Pentru cartile de tip Minion, Hero, si Environment am decis sa fac o
clasa abstracta pentru a genericiza anumite functii care sunt comune la toate. Clasa CardsConvertor
face un fel de wrap in jurul instantei de cardInput primita ca sin input, convertind-o la tipul
Card.
    In pachetul utils am 2 clase, una doar cu constante ("Constants") si una doar cu functii care
au rolul de a transforma datele cartilor in obiecte de tip JSON pentru a putea fi afisate cu
usurinta.