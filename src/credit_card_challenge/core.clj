(ns credit-card-challenge.core
  (:use clojure.pprint)
  (:require [credit-card-challenge.db :as db]
            [credit-card-challenge.model :as md]
            [datomic.api :as d]
            [java-time :as jt]))

;Semana 3

(db/apaga-banco!)
(def conn (db/abre-conexao!))
(db/cria-schema! conn)

(def cliente (md/novo-cliente "Amanda" "11111111" "amanda@gmail.com"))
(pprint cliente)
(pprint @(db/adiciona-cliente! conn [cliente]))


(def cartao (md/novo-cartao (:cliente/id cliente) "2222 2222 2222 2222" "111" "11/2029" 1500.0))
(def cartao2 (md/novo-cartao (:cliente/id cliente) "5555 5555 5555 5555" "112" "11/2030" 8000.0))
(pprint cartao)
(pprint @(db/adiciona-cartao! conn [cartao cartao2]))

(def compra (md/nova-compra  (:cartao/id cartao) (jt/local-date-time 2021 11 18 9 39 30) 25.50 "Mercado Extra" "Alimentação"))
(def compra2 (md/nova-compra  (:cartao/id cartao2) (jt/local-date-time 2021 8 19 9 50 00) 300.50 "Cambly" "Educação"))
(pprint @(db/adiciona-compra! conn [compra compra2]))

(pprint (db/compras-por-cartao (d/db conn) (:cartao/id cartao2)))





