(ns credit-card-challenge.model
  (:require [credit-card-challenge.db :as db])
  (:require [schema.core :as s])
  (:require [java-time :as jt]))

(s/set-fn-validation! true)

(defn uuid [] (java.util.UUID/randomUUID))

(def PosInt (s/pred pos-int? 'inteiro-positivo))

(def PosNum (s/pred #(or (pos? %) (zero? %)) 'numero-positivo))

(def DateTime (class (jt/local-date-time)))

(def Cliente {:id    java.util.UUID,
              :nome  s/Str,
              :cpf   s/Str,
              :email s/Str})

(defn novo-cliente
  [nome cpf email]
  {:cliente/id (uuid)
   :cliente/nome nome
   :cliente/cpf cpf
   :cliente/email email})

(def CartaoDeCredito {:id         java.util.UUID,
                      :id-cliente java.util.UUID,
                      :numero     s/Str,
                      :cvv        s/Str,
                      :validade   s/Str,
                      :limite     s/Num})

(defn novo-cartao
  [id-cliente numero cvv validade limite]
  {:cartao/id (uuid)
   :cartao/id-cliente id-cliente
   :cartao/numero numero
   :cartao/cvv cvv
   :cartao/validade validade
   :cartao/limite limite})

(def Compra {:id              java.util.UUID,
             :id-cartao       java.util.UUID,
             :data            DateTime,
             :valor           PosNum,
             :estabelecimento s/Str,
             :categoria       s/Str})

(def CompraSemIds {(s/optional-key :id) java.util.UUID,
                   (s/optional-key :id-cartao) java.util.UUID,
                   :data            s/Str,
                   :valor           PosNum,
                   :estabelecimento s/Str,
                   :categoria       s/Str})

(def ConjuntoCompraSemIds #{CompraSemIds})

;(s/validate CompraSemIds {:id 1,
;                          :id-cartao 5
;                          :data "21/12/2022 07:20:30",
;                          :valor 300.0,
;                          :estabelecimento "Cambly",
;                          :categoria "Educação"})

(s/def ListaDeCompras [Compra])

(def CategoriaGasto {:categoria   s/Str
                     :gasto-total PosNum})

(def CojuntoCategoriaGasto #{CategoriaGasto})

(def ListaPorCategoria [s/Str ListaDeCompras])

(def Funcs (s/enum > < = <= >= not=))

;(s/validate ListaDeCompras [db/compra1])
;(s/validate ListaDeCompras (db/todas-as-compras))

;(s/validate Compra {:id              1,
;                    :id-cartao       6,
;                    :data            (jt/local-date-time 2021 11 18 9 39 30),
;                    :valor           25.50,
;                    :estabelecimento "Mercado Extra",
;                    :categoria       "Alimentação"})
;(s/validate PosNum -9)
;(println (class (jt/local-date-time)))
