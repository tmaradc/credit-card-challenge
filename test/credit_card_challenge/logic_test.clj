(ns credit-card-challenge.logic-test
  (:require [credit-card-challenge.logic :refer :all])
  (:require [credit-card-challenge.db :refer :all])
  (:require [java-time :as jt])
  (:require [clojure.test :refer :all]
            [credit-card-challenge.db :as db]))

(deftest busca-por-intervalo-fechado-valor-test
  (testing "retorna compras com valores no limite"
    (let [
          compra1 {:id              1,
                   :id-cartao       6,
                   :data            (jt/local-date-time 2021 11 18 9 39 30),
                   :valor           60.0,
                   :estabelecimento "Mercado Extra",
                   :categoria       "Alimentação"},
          compra2 {:id              2,
                   :id-cartao       6,
                   :data            (jt/local-date-time 2021 11 18 9 39 30),
                   :valor           50.0,
                   :estabelecimento "Mercado Extra",
                   :categoria       "Alimentação"},
          compras [compra1 compra2]]
      (is (= [compra1]
             (busca-por-intervalo-fechado-valor 55 60 compras))))))

;(deftest total-por-categoria-test
;  (testing "não pode invocar total por categoria sem simbolos obrigatórios"
;    (is (thrown? clojure.lang.ExceptionInfo
;                 (total-por-categoria nil (todas-as-compras))))
;
;    (is (thrown? clojure.lang.ExceptionInfo
;                 (total-por-categoria "Alimentação" [nil])))
;
;    (is (thrown? clojure.lang.ExceptionInfo
;                 (total-por-categoria nil [nil])))
;
;    (is (thrown? clojure.lang.ExceptionInfo
;                 (total-por-categoria "Educação" [{:id              3,
;                                                   :id-cartao       2,
;                                                   :data            (jt/local-date-time 2022 12 21 7 20 30),
;                                                   :valor           300.00,
;                                                   :estabelecimento "Cambly"
;                                                  }])))
;    ))

(deftest total-lista-compras-test
  (testing "não pode invocar total lista de compras sem simbolos obrigatórios"
    (is (thrown? clojure.lang.ExceptionInfo
                 (total-lista-de-compras [nil])))

    (is (thrown? clojure.lang.ExceptionInfo
                 (total-lista-de-compras [{:id              3,
                                           :id-cartao       2,
                                           :data            (jt/local-date-time 2022 12 21 7 20 30),
                                           :estabelecimento "Cambly",
                                           :categoria       "Educação"}])))))
; primeiro passar nil (nenhuma compra) - ok
; passar uma compra sem alguma keyword - ok
; passar uma compra com um valor inválido para uma keyword
; passar uma compra com todos os atributos certos

; passar nil como lista - ok
; passar lista vazia - ok
; passar um dicionário no lugar da lista


(deftest adicionar-compra-test
  (testing "passar uma compra com atributos corretos"
    (let [compra1 {:id              1,
                   :id-cartao       6,
                   :data            (jt/local-date-time 2021 12 3 7 20 30),
                   :valor           30.00,
                   :estabelecimento "Cambly",
                   :categoria       "Educação"}
          compra2 {:id              2,
                   :id-cartao       6,
                   :data            (jt/local-date-time 2021 12 3 8 20 30),
                   :valor           150.00,
                   :estabelecimento "Cambly",
                   :categoria       "Educação"}
          compra3 {:id              3,
                   :id-cartao       6,
                   :data            (jt/local-date-time 2021 12 3 9 20 30),
                   :valor           60.00,
                   :estabelecimento "Cambly",
                   :categoria       "Educação"}]
      (is (= [compra1 compra2 compra3]
             (adicionar-compra compra3 [compra1 compra2])))))

  (testing "tentar adicionar nil como compra"
    (is (thrown? clojure.lang.ExceptionInfo
                 (adicionar-compra nil [(db/todas-as-compras)]))))

  (testing "passar uma compra sem uma keyword (falta estabelecimento)"
    (is (thrown? clojure.lang.ExceptionInfo
                 (adicionar-compra {:id        3,
                                    :id-cartao 2,
                                    :data      (jt/local-date-time 2022 12 21 7 20 30),
                                    :valor     300.00,
                                    :categoria "Educação"} [(db/todas-as-compras)]))))

  (testing "passar uma compra com um valor inválido para uma keyword (id-cartão não pode ser uma string)"
    (is (thrown? clojure.lang.ExceptionInfo
                 (adicionar-compra {:id              3,
                                    :id-cartao       "2",
                                    :data            (jt/local-date-time 2022 12 21 7 20 30),
                                    :valor           300.00,
                                    :estabelecimento "Cambly",
                                    :categoria       "Educação"} [(db/todas-as-compras)]))))



  (testing "passar uma lista de compras nil"
    (is (thrown? clojure.lang.ExceptionInfo
                 (adicionar-compra {:id              3,
                                    :id-cartao       "2",
                                    :data            (jt/local-date-time 2022 12 21 7 20 30),
                                    :valor           300.00,
                                    :estabelecimento "Cambly",
                                    :categoria       "Educação"} nil))))

  (testing "passar uma lista vazia"
    (is (thrown? clojure.lang.ExceptionInfo
                 (adicionar-compra {:id              3,
                                    :id-cartao       "2",
                                    :data            (jt/local-date-time 2022 12 21 7 20 30),
                                    :valor           300.00,
                                    :estabelecimento "Cambly",
                                    :categoria       "Educação"} []))))

  (testing "passar um dicionário no lugar da lista"
    (is (thrown? clojure.lang.ExceptionInfo
                 (adicionar-compra {:id              3,
                                    :id-cartao       "2",
                                    :data            (jt/local-date-time 2022 12 21 7 20 30),
                                    :valor           300.00,
                                    :estabelecimento "Cambly",
                                    :categoria       "Educação"} {})))))

