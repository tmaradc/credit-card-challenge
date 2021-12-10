(ns credit-card-challenge.core
  (:use clojure.pprint)
  (:require [credit-card-challenge.db :as db]
            [credit-card-challenge.model :as md]))

(db/apaga-banco!)
(def conn (db/abre-conexao!))
(db/cria-schema! conn)

(def cliente (md/novo-cliente "Amanda" "11111111" "amanda@gmail.com"))
(pprint cliente)
(pprint @(db/adiciona-cliente! conn [cliente]))


(def cartao (md/novo-cartao (:cliente/id cliente) "2222 2222 2222 2222" "111" "11/2029" 1500.0))
(pprint cartao)
(pprint @(db/adiciona-cartao! conn [cartao]))




