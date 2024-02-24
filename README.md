# ShoeShop

## Intro

Projektet är tänkt att illustrera en skobutik där information hämtas och skrivs till en databas. 
Fokus har legat på uppkoppling mot **databas** och användning av **lambdas**. Som användare interagerar man med programmet via konsolen.


## Uppbyggnad

Projektet är uppbyggt utifrån den SQL-koden som återfinns i paketet "SQL".
SQL-kodens tabeller har motsvarande javaklasser i paketet "Tables" som i sin tur har hjälpklasser i paketet "Utils". Dessa utility-klasser ansvarar bland annat för att skapa upp objekt motsvarande de i SQL-koden. 

Nedan följer kort info kring de olika paketen och deras klasser/interface samt viktiga detaljer. 

### Paket, klasser & interface
- Configuration
    - LoadProperties, ***Singleton***
    - Settings.properties
      
- Reports
    - Dashboard, ***Högre ordningens funktioner***
    - LookAtReports, **Körbar** klass tänkt för ägare/anställda
    - ProductSearcher, ***Funktionellt Interface***
    - Reports
      
- SQL
    - shoeShop.sql
      
- Tables
    - Belongs, **Mappningstabell** över Category och Product. 
    - Category
    - Customer
    - Product
    - ProductsInOrder, **Mappningstabell** över Product och ShoeOrder.
    - ShoeOrder
      
- TimeToShop
    - PlaceOrder
    - Shopping
    - TimeToShop, **Körbar** klass tänkt för kunder. 
      
- Utils
    - BelongsUtil
    - CategoryUtil
    - CustomerUtil
    - ProductsInOrderUtil
    - ProductUtil
    - ShoeOrderUtil
 
För en visuell bild av alla tabeller, se shoeShopExcel.pdf. 

## Hur man kör koden

### Körbara klasser
Projektet innehåller två "körbara" klasser. Klassen *TimeToShop* i motsvarande paket och klassen *LookAtReports* i paketet Reports. Kör den kundorienterade klassen TimeToShop för att beställa skor som kund och LookAtReports för en överblick på skobutikens statistik tänkt för anställda eller liknande. 

### Installation
För att koden ska gå att köra, dra ned projektet till IntelliJ eller liknande. Kopiera koden i shoeShop.sql och klistra in i MySQL Workbench. Skriv även in rätt krediter i Settings.properties som du hittar i Configuration-paketet. 

För att beställa varor; använd mail och lösenord från någon av kunderna i sql-koden.

_Exempelvis_

Mail: sofia.petterson@gmail.com

Password: xyw_hwo31
