(ns credit-card-challenge.logic
  (:require [credit-card-challenge.db :as db]
            [credit-card-challenge.model :as md]
            [schema.core :as s]
            [java-time :as jt]))

;---------------------------------- COMPRAS -----------------------------------
(s/defn adicionar-compra :- md/ListaDeCompras
  [compra :- md/Compra, lista-de-compras :- md/ListaDeCompras]
  (conj lista-de-compras compra))


(s/defn listar-compras-print :- nil
  [lista-de-compras :- md/ListaDeCompras]
  (doseq [compra lista-de-compras]
    (println "-------------------------------------------")
    ;(println "id:" (:id compra))
    ;(println "id-cartão:" (:id-cartao compra))
    (println "data:" (jt/format "dd/MM/yyyy HH:mm:ss" (:data compra)))
    (println "valor:" (:valor compra))
    (println "estabelecimento:" (:estabelecimento compra))
    (println "categoria:" (:categoria compra))
    (println "\n")))

(s/defn format-data-de-compra :- md/CompraSemIds
  [compra :- md/Compra]
  (update compra :data #(jt/format "dd/MM/yyyy HH:mm:ss" %))
  )

(s/defn listar-compras :- md/ConjuntoCompraSemIds
  [lista-de-compras :- md/ListaDeCompras]
  (->> lista-de-compras
       (map format-data-de-compra)
       (map #(dissoc % :id :id-cartao))
       (into #{})))


;(println (listar-compras (db/todas-as-compras)))

;(println (dissoc {:id 1, :nome "a"} :id ))

;(adicionar-compra {:id              5,
;                   :id-cartao       2,
;                   :data            (jt/local-date-time 2022 12 21 7 20 30),
;                   :valor           900.00,
;                   :estabelecimento "Cambly",
;                   :categoria       "Educação"} (db/todas-as-compras))


;--------------------------- GASTOS POR CATEGORIA ----------------------------
(s/defn total-lista-de-compras :- s/Num
        "Somatorio dos totais da lista compra"
        [lista :- md/ListaDeCompras]
        {:pre (contains? :valor lista)}
        (reduce + (map :valor lista)))

;(println "total-lista-de-compras:" (total-lista-de-compras [nil]))

;(s/defn mesma-categoria-para-todos? :- s/Bool
;  "Verificar se todas as compras da lista possuem a mesma categoria"
;  [categoria lista-de-compras]
;  (->> lista-de-compras
;       (map :categoria)
;       (filter #(not= % categoria))
;       count
;       (= 0)))

(s/defn total-por-categoria :- md/CategoriaGasto
  "Recebe lista de compras agrupadas por categoria e efetua o calculo dos totais de cada categoria"
  [[categoria lista-compras]] :- md/ListaPorCategoria
  ;{:pre [(mesma-categoria-para-todos? categoria lista-compras)]}
  {:categoria   categoria
   :gasto-total (total-lista-de-compras lista-compras)})

(total-por-categoria ["Alimentação" [db/compra1 db/compra2] ] )

(println [db/compra1 db/compra4])

;(println (total-por-categoria "Alimentação" (db/todas-as-compras)))
;(println (total-por-categoria nil (db/todas-as-compras)))
;(println (total-por-categoria "Alimentação" [nil]))
;(println (total-por-categoria nil [nil]))

(s/defn lista-valor-total-por-categoria :- md/CojuntoCategoriaGasto
  "Agrupa compras por categoria fazendo calculo de totais da categoria"
  [lista-de-compras :- md/ListaDeCompras]
  (->> lista-de-compras
       (group-by :categoria)
       (map total-por-categoria)
       (into #{})))

;(println (lista-valor-total-por-categoria (db/todas-as-compras)))
;(println (contains? (db/todas-as-compras) :categoria))

;------------------------------ FATURA DO MÊS -------------------------------
(def data-hora-atual (jt/local-date-time))
(class data-hora-atual)
;(as (local-date) :year :month-of-year)

(s/defn mesmo-mes? :- s/Bool
        "Verifica se o mês da compra é igual ao mês atual"
        [mes-da-compra :- md/Compra]
        (= (jt/month data-hora-atual) (jt/month (:data mes-da-compra))))

(s/defn mesmo-ano? :- s/Bool
        "Verifica se o ano da compra é igual ao ano atual"
        [ano-da-compra :- md/Compra]
        (= (jt/year data-hora-atual) (jt/year (:data ano-da-compra))))

(s/defn mesmo-mes-e-ano? :- s/Bool
        "Verifica se o ano e mes da compra é igual ao ano e mês atual"
        [data-compra :- md/Compra]
        (and (mesmo-ano? data-compra) (mesmo-mes? data-compra)))

(s/defn compras-do-mes :- s/Num
        "Soma total das compras efetuadas no mês"
        ([lista-compras :- md/ListaDeCompras]
         (->> lista-compras
              (filter mesmo-mes-e-ano?)
              total-lista-de-compras))

        ([data :- md/DateTime, lista-compras :- md/ListaDeCompras]
         (->> lista-compras
              (filter #(= (jt/month data) (jt/month (:data %))))
              (filter #(= (jt/year data) (jt/year (:data %))))
              total-lista-de-compras)))


;(println (compras-do-mes (jt/local-date-time 2022 12) (db/todas-as-compras)))

;(println (compras-do-mes (db/todas-as-compras)))

(s/defn fatura-por-cartao :- s/Num
        "Filtra as compras feitas por cartão"
        [id-cartao :- java.util.UUID,
         lista :- md/ListaDeCompras]
        (->> lista
             (filter #(= id-cartao (:id-cartao %)))
             compras-do-mes
             ))

;(println (fatura-por-cartao 2 (db/todas-as-compras)))

;------------------- BUSCA POR VALOR OU ESTABELECIMENTO ---------------------
(s/defn busca-por-estabelecimento :- md/ListaDeCompras
        "Filtra as compras por estabelecimento"
        [estabelecimento :- s/Str,
         lista-de-compras :- md/ListaDeCompras]
        (filter #(= estabelecimento (:estabelecimento %)) lista-de-compras))

;(println (busca-por-estabelecimento "Mercado Extra" (db/todas-as-compras)))

(s/defn busca-por-valor :- md/ListaDeCompras
  "Filtra as compras feitas pelo valor, podendo usar operadores relacionais para indicar o range"
  [lista-de-compras :- md/ListaDeCompras,
   condicao :- md/Funcs,
   valor :- s/Num]
  (filter #(condicao (:valor %) valor) lista-de-compras))

 (let [compras (db/todas-as-compras)]
  (println (busca-por-valor compras = 90.5)))

(s/defn busca-por-intervalo-fechado-valor :- md/ListaDeCompras
        "Filtra as compras feitas pelo range de valores"
        [limite-inferior :- s/Num,
         limite-superior :- s/Num,
         lista-de-compras :- md/ListaDeCompras]
        (-> lista-de-compras
            (busca-por-valor >= limite-inferior)
            (busca-por-valor <= limite-superior)))

;(println (busca-por-intervalo-fechado-valor 200 900 (db/todas-as-compras)))



