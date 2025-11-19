Andmebaasi versioneerimine

Toorik on konfigureeritud kasutama andmebaasi aadressilt jdbc:hsqldb:hsql://localhost/db1
Hsql andmebaasi serveri käivitamiseks on klass app.HsqlDbServer.<br><br>

1. Kontrollige millised andmebaasi skriptid on käivitamise ootel

   ```
   > gradlew clean build flywayInfo
   ``` 
   
2. Käivitage ootel skriptid

   ```
   > gradlew clean build flywayMigrate -i
   ```
   
3. Failis resources/scripts/add_some_data.sql on mõned näidisandmed.<br><br>
  
   a) tehke see skript flyway-le nähtavaks: tõstke kataloogi resources/flyway ja 
      lisage faili nimele versiooni number "V2__";<br>
   b) kontrollige, millised skriptid on käivitunud ja millised ootel (flywayInfo);<br>
   c) käivitage ootel skript (flywayMigrate).<br>

   Failis resources/scripts/alter_person_table.sql skript tabelisse välja lisamiseks<br>
  
   a) tehke see skript flyway-le nähtavaks;<br>
   b) kontrollige, millised skriptid on käivitunud ja millised ootel (flywayInfo);<br>
   c) käivitage ootel skript (flywayMigrate).<br><br>
   
4. Eesmärk on muuta olemasolevat skeemi nii, et isiku ja telefonide seos 
   on määratud vahetabeliga.
   
   Selleks peaks tegema järgmised muudatused.
   
   Soovitatav on iga sammu järel kontrollida, kas see ka õnnestus.<br><br>
   
   a) Lisama telefonide tabelisse id välja (add_id_to_phone.sql)
      Versiooni numbrisse läheb ka sammu number: "V4_1__".<br>

   b) Lisama seoste jaoks uue tabeli (add_joining_table.sql). <br>     

   c) Lisama seostabelisse vastavad kirjed.
      Kirjete lisamiseks kasutage Java koodi. Toorik on klassis tmp.create_joining_entries.
      Flyway on konfigureeritud otsima Java koodi paketist flyway.
      Klassis dev.Main on näide, kuidas migrate() meetodit ise välja kutsuda.<br><br>
      
      Paljude kirjete sisestamiseks sobib meetod SimpleJdbcInsert.executeBatch().<br><br>
      
   d) Kustutage phone tabelist nüüdseks üleliigne person_id väli.<br>
      
5. Paketis app.order on lihtne näide ajalootabelite kasutamise kohta.<br><br>
   
   Klassis Main on kood, mis loob vajalikud tabelid.<br><br>
   
   Teie ülesanne on kirjutada klassi OrderRepository meetod tellimuste 
   salvestamiseks.<br><br>
   
   Uue kirje lisamisel lisatakse see tabelisse orders versiooniga 1.<br><br>
   
   Olemasoleva kirje muutmiseks tuleb vana kirje kopeerida ajalootabelisse 
   ja suurendada orders tabeli kirjel versiooni numbrit.<br><br>
   
   Et vältida paralleelsete muudatustega seotud probleeme peaks muudetava 
   kirje eelnevalt lukustama.<br><br>
   
   Vastavad päringud on toorikus olemas.<br><br>
   
   Kontrollige, et loodud kood töötab.<br><br>
      
6. Paketis app.invoice on lihtne näide ajaloo kirjete samas tabelis hoidmise kohta.<br><br>
   
   Klassis Main on kood, mis loob vajalikud tabelid.<br><br>
   
   Teie ülesanne on kirjutada klassi InvoiceRepository meetod tellimuste 
   salvestamiseks ja andmete pärimiseks.<br><br>
   
   Uue kirje lisamisel lisatakse see tabelisse invoice nii, et start_date 
   on praegune hetk (sql now() funktsioon) ja end_date on null.<br><br>
   
   Olemasoleva kirje muutmisel tuleb vana kirje kopeerida samasse tabelisse
   määrates loodud kirje end_date väärtuseks praeguse hetke.<br><br>
   
   Et vältida paralleelsete muudatustega seotud probleeme peaks muudetava
   kirje eelnevalt lukustama.<br><br>
   
   Vastavad päringud on toorikus olemas.<br><br>
   
   Kontrollige, et loodud kood töötab.<br><br>

7. Paketis builder on klass SqlBuilder, mis võimaldab Sql lauseid koostada.<br><br>
   
   Lisage sellele meetod withQueryTime(LocalDateTime time). See väärtus
   lisatakse "where" ja "join on" tingimuste koostamisel.<br><br>
   
   LocalDateTime objekti saate andmebaasile sobivaks tüübiks teisendada nii:
   java.sql.Timestamp.valueOf(localDateTime);<br><br>

8. Lisage InvoiceRepository klassi ka meetodid kirjete pärimiseks.<br><br>

   findById(Long invoiceId, LocalDateTime moment).<br><br>
   
   Kasutage SqlBuilder klassi eelmisest ülesandest.<br><br>
   
   Meetodi kontrollimiseks on ettevalmistatud mõned näidisandmed (invoice_data.sql).<br><br>
   
   Hetke aja saate nii: LocalDateTime now = LocalDateTime.now().<br><br>

9. Lisage võimlus ka arveridade pärimiseks (tabel invoice_row failis 
   invoice_schema.sql).<br><br>

Lahendused: https://youtu.be/iNrxsKoVgY8
