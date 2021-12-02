(ns credit-card-challenge.logic-test
  (:require [credit-card-challenge.logic :refer :all])
  (:require [credit-card-challenge.db :refer :all])
  (:require [java-time :as jt])
  (:require [clojure.test :refer :all]))

(deftest busca-por-intervalo-fechado-valor-test
  (testing "retorna compras com valores no limite"
    (let [
          compra1 {:id              1,
                   :id-cartao       6,
                   :data            (jt/local-date-time 2021 11 18 9 39 30),
                   :valor           60.0,
                   :estabelecimento "Mercado Extra",
                   :categoria       "Alimentação"},
          compra2  {:id              2,
                    :id-cartao       6,
                    :data            (jt/local-date-time 2021 11 18 9 39 30),
                    :valor           50.0,
                    :estabelecimento "Mercado Extra",
                    :categoria       "Alimentação"},
          compras [compra1 compra2]]
      (is (= [compra1]
             (busca-por-intervalo-fechado-valor 55 60 compras))))))

(deftest total-por-categoria-test
  (testing "não pode invocar total por categoria sem simbolos obrigatórios"
    (is (thrown? clojure.lang.ExceptionInfo
                 (total-por-categoria nil (todas-as-compras))))

    (is (thrown? clojure.lang.ExceptionInfo
                 (total-por-categoria "Alimentação" [nil])))

    (is (thrown? clojure.lang.ExceptionInfo
                 (total-por-categoria nil [nil])))
    ))

