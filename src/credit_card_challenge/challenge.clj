(ns credit_card_challenge.challenge
  (:require [java-time :as jt]))
;(use 'java-time)

;--------------------------------- CLIENTE ---------------------------------
(def cliente {:id    1,
              :nome  "Tamara",
              :cpf   "15426584799",
              :email "tamara@gmail.com"})

(println (:nome cliente))

;---------------------------- CARTÃO DE CRÉDITO ----------------------------
(def cartao-de-credito {:id         2,
                        :id-cliente 1,
                        :numero     "2222 2222 2222 2222",
                        :cvv        "222",
                        :validade   "11/2029",
                        :limite     1500})

(println (:numero cartao-de-credito))

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
              :categoria       "Alimentação"})

(println (:valor compra1))

(defn todas-as-compras []
  [compra1, compra2, compra3, compra4])

(def lista-de-compras [compra1 compra2 compra3 compra4])

(println lista-de-compras)

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

(println (lista-valor-total-por-categoria (todas-as-compras)))

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

(println (compras-do-mes (jt/local-date-time 2022 12) (todas-as-compras)))

(println (compras-do-mes-atual (todas-as-compras)))

(defn fatura-por-cartao
  [id-cartao lista]
  (->> lista
       (filter #(= id-cartao (:id-cartao %)))
       compras-do-mes-atual
       ))

(println (fatura-por-cartao 2 (todas-as-compras)))

;------------------- BUSCA POR VALOR OU ESTABELECIMENTO ---------------------
(defn busca-por-estabelecimento
  [estabelecimento lista-de-compras]
  (filter #(= estabelecimento (:estabelecimento %)) lista-de-compras))

(println (busca-por-estabelecimento "Mercado Extra" (todas-as-compras)))

(defn busca-por-valor
  [lista-de-compras condicao valor]
  (filter #(condicao (:valor %) valor) lista-de-compras))

(let [compras (todas-as-compras)]
  (println (busca-por-valor compras = 90.5)))

(defn busca-por-intervalo-fechado-valor
  [limite-inferior limite-superior lista-de-compras]
  (-> lista-de-compras
      (busca-por-valor >= limite-inferior)
      (busca-por-valor <= limite-superior)))

(println (busca-por-intervalo-fechado-valor 0 100 (todas-as-compras)))