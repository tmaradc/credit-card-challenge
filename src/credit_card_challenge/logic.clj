(ns credit-card-challenge.logic
  (:require [credit-card-challenge.db :as db])
  (:require [java-time :as jt]))

;--------------------------- GASTOS POR CATEGORIA ----------------------------
(defn total-lista-de-compras
  [lista]
  (reduce + (map :valor lista)))

(defn total-por-categoria
  [[categoria lista-compras]]
  {:categoria   categoria
   :gasto-total (total-lista-de-compras lista-compras)})

(defn lista-valor-total-por-categoria
  [lista-de-compras]
  (->> lista-de-compras
       (group-by :categoria)
       (map total-por-categoria)))

;(println (lista-valor-total-por-categoria (db/todas-as-compras)))

;------------------------------ FATURA DO MÊS -------------------------------
(def data-hora-atual (jt/local-date-time))

;(as (local-date) :year :month-of-year)

(defn mesmo-mes?
  [compra]
  (= (jt/month (jt/local-date-time)) (jt/month (:data compra))))

(defn mesmo-ano?
  [compra]
  (= (jt/year (jt/local-date-time)) (jt/year (:data compra))))

(defn compras-do-mes-atual
  [lista-compras]
  (->> lista-compras
       (filter mesmo-ano?)
       (filter mesmo-mes?)
       total-lista-de-compras))

(defn compras-do-mes
  [data lista-compras]
  (->> lista-compras
       (filter #(= (jt/month data) (jt/month (:data %))))
       (filter #(= (jt/year data) (jt/year (:data %))))
       total-lista-de-compras))

;(println (compras-do-mes (jt/local-date-time 2022 12) (db/todas-as-compras)))

;(println (compras-do-mes-atual (db/todas-as-compras)))

(defn fatura-por-cartao
  [id-cartao lista]
  (->> lista
       (filter #(= id-cartao (:id-cartao %)))
       compras-do-mes-atual
       ))

;(println (fatura-por-cartao 2 (db/todas-as-compras)))

;------------------- BUSCA POR VALOR OU ESTABELECIMENTO ---------------------
(defn busca-por-estabelecimento
  [estabelecimento lista-de-compras]
  (filter #(= estabelecimento (:estabelecimento %)) lista-de-compras))

;(println (busca-por-estabelecimento "Mercado Extra" (db/todas-as-compras)))

(defn busca-por-valor
  [lista-de-compras condicao valor]
  (filter #(condicao (:valor %) valor) lista-de-compras))

; (let [compras (db/todas-as-compras)]
;  (println (busca-por-valor compras = 90.5)))

(defn busca-por-intervalo-fechado-valor
  [limite-inferior limite-superior lista-de-compras]
  (-> lista-de-compras
      (busca-por-valor >= limite-inferior)
      (busca-por-valor <= limite-superior)))

;(println (busca-por-intervalo-fechado-valor 200 900 (db/todas-as-compras)))