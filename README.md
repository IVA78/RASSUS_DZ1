# Prva laboratorijska vježba - Prikupljanje i obrada senzorskih podataka

Ovaj projekt predstavlja prvi laboratorijski zadatak iz kolegija *Raspodijeljeni sustavi*, akademske godine 2024./2025. Cilj vježbe je izraditi distribuirani sustav temeljen na arhitekturi klijent-poslužitelj koji prikuplja, obrađuje i kalibrira senzorske podatke o okolišu koristeći gRPC za komunikaciju.

## Tehnologije
- **Jezik**: Java
- **Build alat**: Gradle
- **gRPC**: Za komunikaciju između senzora
- **Spring Boot**: Poslužiteljski dio aplikacije
- **Logger**: Za bilježenje dolaznih i odlaznih podataka

## Arhitektura sustava
Distribuirani sustav temelji se na arhitekturi klijent-poslužitelj i koristi dvije ključne metode komunikacije:
1. gRPC za razmjenu podataka među senzorima: Omogućava brzu i učinkovitu razmjenu očitanja između senzora u realnom vremenu.
2. REST za komunikaciju između senzora i poslužitelja: REST API omogućava registraciju senzora, dohvaćanje najbližeg susjeda i pohranu kalibriranih očitanja na poslužitelju.

Svaki senzor radi kao zaseban proces s jedinstvenim portom i koordinatama te komunicira sa susjednim senzorima za razmjenu očitanja.

## Funkcionalnosti senzora
- **Inicijalizacija i registracija**: Senzor generira nasumične geografske koordinate unutar zadanog raspona i registrira se na poslužitelju.
- **Generiranje i razmjena očitanja**: Senzori periodično generiraju podatke o temperaturi, tlaku, vlazi i koncentraciji plinova (CO, NO2 ili SO2) te ih pohranjuju na poslužitelju.
- **Kalibracija podataka**: Razmjena očitanja s najbližim susjednim senzorom za preciznija mjerenja.
- **Logiranje i obrada grešaka**: Senzori i poslužitelj bilježe podatke i eventualne greške za lakše otklanjanje problema.
- **Pohrana podataka**: Kalibrirana očitanja šalju se poslužitelju za daljnju obradu i pohranu.

  ## Funkcionalnosti poslužitelja
  - **Registracija senzora**: Omogućuje registraciju senzora i čuva njihove podatke, uključujući geografski položaj i trenutne očitanja.
  - **Dohvaćanje najbližeg susjeda**: Poslužitelj izračunava najbliži senzor za klijenta koristeći Haversineovu formulu za geografske koordinate.
  - **Pohrana i dohvaćanje očitanja**: Omogućuje pohranu i dohvaćanje senzorskih očitanja u JSON formatu.

## Dodatne informacije
- [gRPC dokumentacija](https://grpc.io/docs/)
- [Gradle dokumentacija](https://docs.gradle.org/current/userguide/userguide.html)


## Autor
- Iva Svalina
- Fakultet elektrotehnike i računarstva, Sveučilište u Zagrebu
