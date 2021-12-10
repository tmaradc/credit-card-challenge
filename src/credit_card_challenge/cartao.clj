;(ns credit-card-challenge.cartao
;  (:use clojure.pprint)
;  (:require [credit-card-challenge.db :as db]
;            [credit-card-challenge.model :as md]
;            [credit-card-challenge.cliente :as c]))
;
;(db/apaga-banco!)
;(def conn (db/abre-conexao!))
;(db/cria-schema! conn)
;
;
;(def cartao (md/novo-cartao (:id c/cliente)))
;
;(pprint (db/adiciona-cliente! conn [cliente]))
