(ns credit-card-challenge.logic
  (:require [credit-card-challenge.db :as db])
  (:require [java-time :as jt]))

;--------------------------- GASTOS POR CATEGORIA ----------------------------
(defn total-lista-de-compras
  "Somatorio dos totais da lista compra"
  [lista]
  (reduce + (map :valor lista)))

(defn total-por-categoria
  "Recebe lista de compras agrupadas por categoria e efetua o calculo dos totais de cada categoria"
  [[categoria lista-compras]]
  {:categoria   categoria
   :gasto-total (total-lista-de-compras lista-compras)})

(defn lista-valor-total-por-categoria
  "Agrupa compras por categoria fazendo calculo de totais da categoria"
  [lista-de-compras]
  (->> lista-de-compras
       (group-by :categoria)
       (map total-por-categoria)))

;(println (lista-valor-total-por-categoria (db/todas-as-compras)))

;------------------------------ FATURA DO MÊS -------------------------------
(def data-hora-atual (jt/local-date-time))
;(as (local-date) :year :month-of-year)

(defn mesmo-mes?
  "Verifica se o mês da compra é igual ao mês atual"
  [mes-da-compra]
  (= (jt/month data-hora-atual) (jt/month (:data mes-da-compra))))

(defn mesmo-ano?
  "Verifica se o ano da compra é igual ao ano atual"
  [ano-da-compra]
  (= (jt/year data-hora-atual) (jt/year (:data ano-da-compra))))

(defn mesmo-mes-e-ano?
  "Verifica se o ano e mes da compra é igual ao ano e mês atual"
  [data-compra]
  (and (mesmo-ano? data-compra) (mesmo-mes? data-compra)))

(defn compras-do-mes
  "Soma total das compras efetuadas no mês"
  ([lista-compras]
   (->> lista-compras
        (filter mesmo-mes-e-ano?)
        total-lista-de-compras))
  ([data lista-compras]
   (->> lista-compras
        (filter #(= (jt/month data) (jt/month (:data %))))
        (filter #(= (jt/year data) (jt/year (:data %))))
        total-lista-de-compras)))


;(println (compras-do-mes (jt/local-date-time 2022 12) (db/todas-as-compras)))

;(println (compras-do-mes-atual (db/todas-as-compras)))

(defn fatura-por-cartao
  "Filtra as compras feitas por cartão"
  [id-cartao lista]
  (->> lista
       (filter #(= id-cartao (:id-cartao %)))
       compras-do-mes
       ))

;(println (fatura-por-cartao 2 (db/todas-as-compras)))

;------------------- BUSCA POR VALOR OU ESTABELECIMENTO ---------------------
(defn busca-por-estabelecimento
  "Filtra as compras por estabelecimento"
  [estabelecimento lista-de-compras]
  (filter #(= estabelecimento (:estabelecimento %)) lista-de-compras))

;(println (busca-por-estabelecimento "Mercado Extra" (db/todas-as-compras)))

(defn busca-por-valor
  "Filtra as compras feitas pelo valor, podendo usar operadores relacionais para indicar o range"
  [lista-de-compras condicao valor]
  (filter #(condicao (:valor %) valor) lista-de-compras))

; (let [compras (db/todas-as-compras)]
;  (println (busca-por-valor compras = 90.5)))

(defn busca-por-intervalo-fechado-valor
  "Filtra as compras feitas pelo range de valores"
  [limite-inferior limite-superior lista-de-compras]
  (-> lista-de-compras
      (busca-por-valor >= limite-inferior)
      (busca-por-valor <= limite-superior)))

;(println (busca-por-intervalo-fechado-valor 200 900 (db/todas-as-compras)))