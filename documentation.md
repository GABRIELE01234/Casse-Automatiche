# API G.D.O

* [READ ALL](#read-all)

* [CREAZIONE SCONTRINO](#1-creazione-scontrino)

* [STOCK-FINALE](#2-stock-finale)

* [INCASSO GIORNALIERO](#3-incasso-giornaliero)

* [RESOCONTO ARTICOLO](#4-resoconto-articolo)

* [INCASSO PER REPARTO DATO UN ANNO](#5-incasso-per-reparto-dato-un-anno)

* [INCASSO PER REPARTO DATO UNA GIORNATA](#6-incasso-per-reparto-data-una-giornata)

* [CREA REPARTO](#7-crea-reparto)

* [INSERISCI NUOVO ARTICOLO](#8-inserisci-nuovo-articolo)

* [GENERA BARCODE](#9-genera-un-barcode-con-un-inizio-validita-ed-una-fine-e-lo-associa-ad-un-articolo)

*Simulazione Casse Automatiche*



Scrivere un programma java per gestire le casse automatiche,

questo tipo di sistemi sia nei grandi che nei piccoli punti vendita della GDO ed è possibile utilizzarli sia che si stia acquistando un solo prodotto,

sia per spese più corpose. L'applicazione dovrà gestire i dati anagrafici degli articoli quali:



- Anagrafica: nome articolo, grammatura (intero), unità di misura (kg, g, pz), reparto

- Barcode: codice barcode associato all'articolo. Deve prevedere l'inizio e fine validità. Un articolo può essere associato a più
  barcode

- Prezzo: prezzo di vendita. Il prezzo può variare nel tempo

- Stock: indica lo stock all'inizio di ogni giornata



Caratteristiche del Quesito:

•            Utilizzo di Java >= 1.7

•            Utilizzo di Db a piacere

•            Utilizzo delle collection per immagazzinare i dati estratti dal Db

•            Utilizzo di almeno una funzione lambda*



NB : Non è obbligatorio utilizzare framework in particolare (Hibernate, Jpa, Spring, SpringBoot) e lasciamo allo sviluppatore la decisione.



Attività da completare :

- Progettazione della Base Dati

- Creare un endpoint che permetta di creare uno scontrino

- Dato un barcode, registrare un articolo all'interno dello scontrino. è possibile inserire lo stesso articolo più volte

- Dati gli scontrini, calcolare lo stock a fine giornata

- Rendere disponibile un endpoint che calcoli l'incasso della giornata

- Rendere disponibile un endpoint che calcoli, per ogni articolo, i pezzi venduti e l'incasso dell'articolo, data una giornata.

- Rendere disponibile un endpoint che calcoli l'incasso per reparto, data una giornata

- Rendere disponibile un endpoint che calcoli l'incasso per reparto, dato un anno

## READ ALL

| ID | METHOD | API                                                                                                                      |                 TABLE                 |
|:--:|:------:|--------------------------------------------------------------------------------------------------------------------------|:-------------------------------------:|
| 1  |  POST  | http://localhost:8080/api/v1/creazione-scontrino                                                                         |          CREAZIONE SCONTRINO          |
| 2  |  GET   | http://localhost:8080/api/v1/stock-finale?dataParametro=28/01/2024&idArticolo=a412665d-3343-4e94-a975-c2f3cc7d8bf1       |             STOCK-FINALE              |
| 3  |  GET   | http://localhost:8080/api/v1/incasso                                                                                     |          INCASSO GIORNALIERO          |
| 4  |  GET   | http://localhost:8080/api/v1/resoconto-articolo?idArticolo=a412665d-3343-4e94-a975-c2f3cc7d8bf1&data=2024-01-28          |          RESOCONTO ARTICOLO           |
| 5  |  GET   | http://localhost:8080/api/v1/incasso-per-reparto-by-anno?a=2024&idReparto=67465f37-337f-4c12-bf34-96e00b43b6f6           |   INCASSO PER REPARTO DATO UN ANNO    |
| 6  |  GET   | http://localhost:8080/api/v1/incasso-per-reparto?dataParametro=27/01/2024&idReparto=67465f37-337f-4c12-bf34-96e00b43b6f6 | INCASSO PER REPARTO DATO UNA GIORNATA |
| 7  |  POST  | http://localhost:8080/api/v1/crea-reparto                                                                                |             CREA REPARTO              |
| 8  |  POST  | http://localhost:8080/api/v1/inserisci-articolo                                                                          |       INSERISCI NUOVO ARTICOLO        |
| 9  |  POST  | http://localhost:8080/api/v1/genera-barcode                                                                              |            GENERA BARCODE             |

### REQUEST AND RESPONSE EXAMPLE

#### 

## 1. CREAZIONE SCONTRINO

| METHOD | API                                               |        TABLE        |
|:------:|---------------------------------------------------|:-------------------:|
|  POST  | http://localhost:8080/api/v1/creazione-scontrino  | CREAZIONE SCONTRINO |

* **RESPONSE:** *201 CREATED*

#### BODY REQUEST

```json
[
    {
        "barcode": "1fb10652",
        "quantita": 5
    }, 
       ...
]
```

### 

## 2. STOCK-FINALE

| METHOD | API                                                                                                                 |    TABLE     |
|:------:|---------------------------------------------------------------------------------------------------------------------|:------------:|
|  GET   | http://localhost:8080/api/v1/stock-finale?dataParametro=28/01/2024&idArticolo=a412665d-3343-4e94-a975-c2f3cc7d8bf1  | STOCK FINALE |

* **RESPONSE:** *Float Object*

#### PARAM REQUEST

```json
    {
        "param1": "dataParametro",
        "param2": "idArticolo"
    }
```

### 

## 3. INCASSO GIORNALIERO

| METHOD | API                                   |        TABLE         |
|:------:|---------------------------------------|:--------------------:|
|  GET   | http://localhost:8080/api/v1/incasso  | INCASSO GIORNALIERO  |

* **RESPONSE:** *Float Object*

#### 4. RESOCONTO ARTICOLO

| METHOD | API                                                                                                              |        TABLE        |
|:------:|------------------------------------------------------------------------------------------------------------------|:-------------------:|
|  GET   | http://localhost:8080/api/v1/resoconto-articolo?idArticolo=a412665d-3343-4e94-a975-c2f3cc7d8bf1&data=2024-01-28  | RESOCONTO ARTICOLO  |

* **RESPONSE:** *Map<String,Object> mapObject*

#### PARAM REQUEST : 'Articolo e Data'

```json
    {
      "param1": "idArticolo",
      "param2": "data"
    }
```

#### 5. INCASSO PER REPARTO DATO UN ANNO

| METHOD | API                                                                                                             |              TABLE               |
|:------:|-----------------------------------------------------------------------------------------------------------------|:--------------------------------:|
|  GET   | http://localhost:8080/api/v1/incasso-per-reparto-by-anno?a=2024&idReparto=67465f37-337f-4c12-bf34-96e00b43b6f6  | INCASSO PER REPARTO DATO UN ANNO |

* **RESPONSE:** *Float Object*

#### PARAM REQUEST : 'Reparto e Anno'

```json

  {
    "param1": "a",
    "param2": "idReparto"
  }
```

#### 6. INCASSO PER REPARTO DATA UNA GIORNATA


| METHOD | API                                                                                                                       |                 TABLE                 |
|:------:|---------------------------------------------------------------------------------------------------------------------------|:-------------------------------------:|
|  GET   | http://localhost:8080/api/v1/incasso-per-reparto?dataParametro=27/01/2024&idReparto=67465f37-337f-4c12-bf34-96e00b43b6f6  | INCASSO PER REPARTO DATA UNA GIORNATA |

* **RESPONSE:** *Float Object*

#### PARAM REQUEST : 'Reparto e Giorno'

```json
  {
    "param1": "dataParametro",
    "param2": "idReparto"
  }
```

#### 7. CREA REPARTO


| METHOD | API                                         |    TABLE     |
|:------:|---------------------------------------------|:------------:|
|  POST  | http://localhost:8080/api/v1/crea-reparto   | CREA REPARTO |

* **RESPONSE:** *201 CREATED*

```json
  {
    "nomeReparto": "ELETTRONICA"
  }
```


#### 8. INSERISCI NUOVO ARTICOLO


| METHOD | API                                              |                 TABLE                  |
|:------:|--------------------------------------------------|:--------------------------------------:|
|  POST  | http://localhost:8080/api/v1/inserisci-articolo  | INSERISCI UN NUOVO ARTICOLO IN REPARTO |

* **RESPONSE:** *201 HttpStatus.CREATED*

#### BODY REQUEST : 'class ArticoloRequest'

```json
{
  "nome":"Mascara L'Oreàl Paris",
  "grammatura":"1",
  "unitaDiMisura":"pz",
  "idReparto":"ae631364-4058-4b6d-a2a1-cf8739a9fe64",
  "prezzo":9.99,
  "stock":20
}
```


#### 9. GENERA UN BARCODE CON UN INIZIO VALIDITA' ED UNA FINE, E LO ASSOCIA AD UN ARTICOLO


| METHOD | API                                          |       TABLE       |
|:------:|----------------------------------------------|:-----------------:|
|  GET   | http://localhost:8080/api/v1/genera-barcode  | GENERA UN BARCODE |

* **RESPONSE:** *201 HttpStatus.CREATED*

#### BODY REQUEST : 'class GenerateBarcodeRequest'

```json
{
  "inizioValidita":"2024-02-27",
  "fineValidita":"2024-03-31",
  "idArticolo":"bc6c091e-78f4-43ab-bcfb-8da5abe8956c"
}
```



