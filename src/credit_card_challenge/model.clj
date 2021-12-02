(ns credit-card-challenge.model
  (:require [credit-card-challenge.db :as db])
  (:require [schema.core :as s])
  (:require [java-time :as jt]))

(s/set-fn-validation! true)

;TODO: Depois mudar o id para Uuid

(def PosInt (s/pred pos-int? 'inteiro-positivo))
(def DateTime (class (jt/local-date-time)))

(def Cliente {:id    PosInt,
              :nome  s/Str,
              :cpf   s/Str,
              :email s/Str})

(def CartaoDeCredito {:id         PosInt,
                      :id-cliente PosInt,
                      :numero     s/Str,
                      :cvv        s/Str,
                      :validade   s/Str,
                      :limite     s/Num})

(def Compra {:id              PosInt,
             :id-cartao       PosInt,
             :data            DateTime,
             :valor           s/Num,
             :estabelecimento s/Str,
             :categoria       s/Str})

(s/def ListaDeCompras [Compra])

(def CategoriaGasto {:categoria   s/Str
                     :gasto-total s/Num})

(def ListaCategoriaGasto [CategoriaGasto])

;(s/validate ListaDeCompras [db/compra1])
;(s/validate ListaDeCompras (db/todas-as-compras))

;(s/validate Compra {:id              1,
;                    :id-cartao       6,
;                    :data            (jt/local-date-time 2021 11 18 9 39 30),
;                    :valor           25.50,
;                    :estabelecimento "Mercado Extra",
;                    :categoria       "Alimentação"})
;
;(println (class (jt/local-date-time)))

