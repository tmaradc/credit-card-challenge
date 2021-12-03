(ns credit-card-challenge.db
  (:require [java-time :as jt]))

;--------------------------------- CLIENTE ---------------------------------
(def usuario {:id    1,
              :nome  "Tamara",
              :cpf   "15426584799",
              :email "tamara@gmail.com"})

(defn cliente [] usuario)

;---------------------------- CARTÃO DE CRÉDITO ----------------------------
(def cartao-de-credito {:id         2,
                        :id-cliente 1,
                        :numero     "2222 2222 2222 2222",
                        :cvv        "222",
                        :validade   "11/2029",
                        :limite     1500})

(defn cartao [] cartao-de-credito)

;---------------------------- LISTA DE COMPRAS -----------------------------

(def compra1 {:id              1,
              :id-cartao       6,
              :data            (jt/local-date-time 2021 11 18 9 39 30),
              :valor           25.50,
              :estabelecimento "Mercado Extra",
              :categoria       "Alimentação"})

(def compra2 {:id              2,
              :id-cartao       2,
              :data            (jt/local-date-time 2021 11 20 10 50 00),
              :valor           90.50,
              :estabelecimento "Pacheco",
              :categoria       "Saúde"})

(def compra3 {:id              3,
              :id-cartao       2,
              :data            (jt/local-date-time 2022 12 21 7 20 30),
              :valor           300.00,
              :estabelecimento "Cambly",
              :categoria       "Educação"})

(def compra4 {:id              4,
              :id-cartao       2,
              :data            (jt/local-date-time 2022 12 22 9 39 30),
              :valor           30.50,
              :estabelecimento "Mercado Extra",
              :categoria       "Alimentação"
              }
  )

(defn todas-as-compras []
  [compra1, compra2, compra3, compra4])


(+ 300.00 30.50 90.50 25.50)