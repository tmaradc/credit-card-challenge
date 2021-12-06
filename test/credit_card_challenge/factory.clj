(ns credit-card-challenge.factory
  (:require [java-time :as jt]))

(def compra1 {:id              1,
              :id-cartao       6,
              :data            (jt/local-date-time 2021 12 3 7 20 30),
              :valor           30.00,
              :estabelecimento "Cambly",
              :categoria       "Educação"})

(def compra1SemId {:data            "03/12/2021 07:20:30",
                   :valor           30.00,
                   :estabelecimento "Cambly",
                   :categoria       "Educação"})

(def compra2 {:id              2,
              :id-cartao       6,
              :data            (jt/local-date-time 2021 10 3 8 20 30),
              :valor           150.00,
              :estabelecimento "Cambly",
              :categoria       "Educação"})

(def compra2SemId {:data            "03/10/2021 08:20:30",
                   :valor           150.00,
                   :estabelecimento "Cambly",
                   :categoria       "Educação"})

(def compra3 {:id              3,
              :id-cartao       6,
              :data            (jt/local-date-time 2021 12 3 9 20 30),
              :valor           60.00,
              :estabelecimento "Cambly",
              :categoria       "Educação"})

(def compra3SemId {:data            "03/12/2021 09:20:30",
                   :valor           60.00,
                   :estabelecimento "Cambly",
                   :categoria       "Educação"})

(def compra4 {:id              1,
              :id-cartao       6,
              :data            (jt/local-date-time 2021 11 3 7 20 30),
              :valor           30.00,
              :estabelecimento "Pacheco",
              :categoria       "Saúde"})


(def compra5 {:id              2,
              :id-cartao       6,
              :data            (jt/local-date-time 2021 10 3 8 20 30),
              :valor           150.00,
              :estabelecimento "Cambly",
              :categoria       "Educação"})

(def compra6 {:id              3,
              :id-cartao       6,
              :data            (jt/local-date-time 2021 12 3 9 20 30),
              :valor           60.00,
              :estabelecimento "Cambly",
              :categoria       "Educação"})

(def compraSemKeyWordCategoria {:id              1,
                                :id-cartao       6,
                                :data            (jt/local-date-time 2021 12 3 7 20 30),
                                :valor           30.00,
                                :estabelecimento "Pacheco"})

(def compraSemKeyWordValor {:id              2,
                            :id-cartao       6,
                            :data            (jt/local-date-time 2021 12 3 8 20 30),
                            :estabelecimento "Cambly",
                            :categoria       "Educação"})

(def compraComValorErradoAssociadoAoKeyword {:id              3,
                                             :id-cartao       "2",
                                             :data            (jt/local-date-time 2022 12 21 7 20 30),
                                             :valor           300.00,
                                             :estabelecimento "Cambly",
                                             :categoria       "Educação"})

(def compraComValorNegativo {:id              3,
                                             :id-cartao       "2",
                                             :data            (jt/local-date-time 2022 12 21 7 20 30),
                                             :valor           -300.00,
                                             :estabelecimento "Cambly",
                                             :categoria       "Educação"})

(def listaDeComprasValida  [compra1 compra2 compra3])
(def listaDeComprasValida2 [compra4 compra5 compra6])
