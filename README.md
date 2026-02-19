# Back del proyecto TallerPruebas

## Enlaces de interés

* [BookstoreBack](https://github.com/Uniandes-isis2603/bookstore-back) -> Repositorio de referencia para el Back

* [Jenkins](http://157.253.238.75:8080/jenkins-isis2603/) -> Autentíquese con sus credencias de GitHub
* [SonarQube](http://157.253.238.75:8080/sonar-isis2603/) -> No requiere autenticació

 ESCENARIOS REGLA 1
 1 EXITO: Se mueve dinero al bolsillo cuando.el saldo de la cuenta es mayor o igual al monto
 fallo : El saldo a mover desde la cuenta es menor al monto
 fallo: Que el bolsillo no exista
 fallo que la cuenta no exista.
 Fallo  ingresar un caracter no numerico 
 

 ESCENERIO REGLA 2
 EXITO:  que la cuenta origen y destino existan
 Exito confirmar que la cuenta origen y la destino no sena iguales.
 FALLO: Que la cuenta origen o la cuenta destino no existan
 Fallo: que la cuenta origen y la destina sean iguales
 exito : que los fondos sean suficientes en el origen 

|Escenario| Estado Inicial (BD) | Acción (Input) | Resultado Esperado(Output/BD) |
|---------|---------------------|----------------|-------------------------------|
|Exito:se mueve dinero el bolsillo cuando el saldo de la cuenta es mayor o igual al monto|