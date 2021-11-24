(ns credit_card_challenge.challenge
  (:require [credit-card-challenge.db :as db])
  (:require [credit-card-challenge.logic :as l])
  (:require [java-time :as jt]))

(println "Lista de compras: ")
(println (db/todas-as-compras))

(println "\nValor total por Categoria: ")
(println (l/lista-valor-total-por-categoria (db/todas-as-compras)))

(println "\nTotal das compras do mês de dezembro: "
         (l/compras-do-mes (jt/local-date-time 2022 12) (db/todas-as-compras)))

(println "\nTotal das compras do mês atual:"
         (l/compras-do-mes-atual (db/todas-as-compras)))

(println "\nFatura do mês atual filtrado por cartão:"
         (l/fatura-por-cartao 2 (db/todas-as-compras)))

(println "\nBusca pelo estabelecimento Mercado Extra: ")
(println (l/busca-por-estabelecimento "Mercado Extra" (db/todas-as-compras)))

(println "\nBusca por valor (compras com o valor igual a 90.5): ")
(let [compras (db/todas-as-compras)]
  (println (l/busca-por-valor compras = 90.5)))

(println "\nBusca por valor (intervalo fechado de 200 a 900): ")
(println (l/busca-por-intervalo-fechado-valor 200 900 (db/todas-as-compras)))
