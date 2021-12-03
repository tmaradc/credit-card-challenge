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

;
;
(deftest listar-compras-test

  (testing "passar uma lista vazia"
    (is (= #{} (lista-valor-total-por-categoria [])))
    (is (= #{} (lista-valor-total-por-categoria nil))))

  )

(deftest lista-valor-total-por-categoria-test
  (testing "passar uma lista com valores válidos"
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
      (is (= #{ {:categoria "Educação", :gasto-total 240.0} }
             (lista-valor-total-por-categoria [compra1 compra2 compra3])))))

  (testing "passar uma lista com valores válidos 2"
    (let [compra1 {:id              1,
                   :id-cartao       6,
                   :data            (jt/local-date-time 2021 12 3 7 20 30),
                   :valor           30.00,
                   :estabelecimento "Pacheco",
                   :categoria       "Saúde"}
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
      (is (= #{{:categoria "Saúde", :gasto-total 30.0}
               {:categoria "Educação", :gasto-total 210.0}}
             (lista-valor-total-por-categoria [compra2 compra3 compra1])))))

  (testing "passar uma compra na lista inválida (sem o keyword de categoria)"
    (let [compra1 {:id              1,
                   :id-cartao       6,
                   :data            (jt/local-date-time 2021 12 3 7 20 30),
                   :valor           30.00,
                   :estabelecimento "Pacheco"}
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
      (is (thrown? clojure.lang.ExceptionInfo
             (lista-valor-total-por-categoria [compra2 compra3 compra1])))))

  (testing "passar uma compra na lista inválida (sem o keyword de valor)"
    (let [compra1 {:id              1,
                   :id-cartao       6,
                   :data            (jt/local-date-time 2021 12 3 7 20 30),
                   :valor           30.00,
                   :estabelecimento "Pacheco",
                   :categoria       "Saúde"}
          compra2 {:id              2,
                   :id-cartao       6,
                   :data            (jt/local-date-time 2021 12 3 8 20 30),
                   :estabelecimento "Cambly",
                   :categoria       "Educação"}
          compra3 {:id              3,
                   :id-cartao       6,
                   :data            (jt/local-date-time 2021 12 3 9 20 30),
                   :valor           60.00,
                   :estabelecimento "Cambly",
                   :categoria       "Educação"}]
      (is (thrown? clojure.lang.ExceptionInfo
                   (lista-valor-total-por-categoria [compra2 compra3 compra1])))))

  (testing "passar uma lista vazia"
    (is (= #{} (lista-valor-total-por-categoria [])))
    (is (= #{} (lista-valor-total-por-categoria nil))))

  )

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

