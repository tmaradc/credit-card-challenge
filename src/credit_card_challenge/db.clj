(ns credit-card-challenge.db
  (:require [java-time :as jt]
            [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/credit-card")

(defn uuid [] (java.util.UUID/randomUUID))
(def schema [
             ; Cliente
             {:db/ident       :cliente/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :cliente/nome
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O nome de um cliente"}
             {:db/ident       :cliente/cpf
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O cpf de um cliente"}
             {:db/ident       :cliente/email
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O email de um cliente"}

             ; Cartão de Crédito
             {:db/ident       :cartao/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :cartao/id-cliente
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one}
             {:db/ident       :cartao/cvv
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O cvv do cartão"}
             {:db/ident       :cartao/validade
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "A validade do cartão"}
             {:db/ident       :cartao/numero
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O numero do cartão"}
             {:db/ident       :cartao/limite
              :db/valueType   :db.type/double
              :db/cardinality :db.cardinality/one
              :db/doc         "O limite do cartão"}

             ;Compra
             {:db/ident       :compra/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :compra/id-cartao
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/doc         "O id do cartão que foi efetuada a compra"}
             {:db/ident       :compra/valor
              :db/valueType   :db.type/double
              :db/cardinality :db.cardinality/one
              :db/doc         "O valor da compra"}
             {:db/ident       :compra/estabelecimento
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O estabelecimento da compra"}
             {:db/ident       :compra/categoria
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "A categoria da compra"}
             {:db/ident       :compra/data
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O data da compra"}
             ])

(defn abre-conexao! []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn apaga-banco! []
  (d/delete-database db-uri))

(defn cria-schema! [conn]
  (d/transact conn schema))

(defn adiciona-cliente!
  [conn clientes]
  (d/transact conn clientes))

(defn adiciona-cartao!
  [conn cartoes]
  (d/transact conn cartoes))

(defn adiciona-compra!
  [conn cartoes]
  (let [cartoes (map #(update % :compra/data jt/format) cartoes)]
    (d/transact conn cartoes)))

(defn compras-por-cartao
  [conn id-cartao]
  (let [compras (d/q '[:find [(pull ?entidade [*]) ...]
               :in $ ?cartao
               :where [?entidade :compra/id-cartao ?cartao]]
             conn id-cartao)]
    (map #(update % :compra/data jt/local-date-time) compras)))



;(pprint @(d/transact conn [[:db/add id-entidade :produto/preco 0.1M]]))
;[:db/add [:produto/id (:produto/id produto)] :produto/categoria [:categoria/id (:categoria/id categoria)]]
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