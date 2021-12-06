(ns credit-card-challenge.logic-test
  (:require [credit-card-challenge.logic :refer :all]
            [credit-card-challenge.db :refer :all]
            [java-time :as jt]
            [clojure.test :refer :all]
            [credit-card-challenge.db :as db]
            [credit-card-challenge.factory :as ex]))


(deftest busca-por-intervalo-fechado-valor-test
  (testing "retorna compras com valores no limite"
    (let [compras [ex/compra1 ex/compra2]]
      (is (= [ex/compra1]
             (busca-por-intervalo-fechado-valor (:valor compra1) 60 compras))))))

(deftest listar-compras-test
  (testing "passar uma lista com valores válidos"
      (is (= #{ex/compra1SemId ex/compra2SemId ex/compra3SemId}
             (listar-compras [ex/compra1 ex/compra2 ex/compra3]))))

  (testing "passar uma compra sem ID"
    (is (thrown? clojure.lang.ExceptionInfo
           (listar-compras [ex/compra1SemId ex/compra2 ex/compra3]))))

  (testing "passar uma lista de compras com uma compra sem o keyword valor"
    (is (thrown? clojure.lang.ExceptionInfo
                 (listar-compras [ex/compraSemKeyWordValor ex/compra2 ex/compra3]))))

  (testing "passar uma lista de compras com uma compra com o valor negativo"
    (is (thrown? clojure.lang.ExceptionInfo
                 (listar-compras [ex/compraComValorNegativo ex/compra2 ex/compra3]))))

  (testing "passar uma lista vazia"
    (is (= #{} (listar-compras [])))
    (is (= #{} (listar-compras nil))))
  )

(deftest lista-valor-total-por-categoria-test
  (testing "passar uma lista com valores válidos"
      (is (= #{{:categoria "Educação", :gasto-total 240.0}}
             (lista-valor-total-por-categoria [ex/compra1 ex/compra2 ex/compra3]))))

  (testing "passar uma lista com valores válidos 2"
      (is (= #{{:categoria "Saúde", :gasto-total 30.0}
               {:categoria "Educação", :gasto-total 210.0}}
             (lista-valor-total-por-categoria [ex/compra4 ex/compra5 ex/compra6]))))

  (testing "passar uma compra na lista inválida (sem o keyword de categoria)"
      (is (thrown? clojure.lang.ExceptionInfo
                   (lista-valor-total-por-categoria [ex/compra2 ex/compra3 ex/compraSemKeyWordCategoria]))))

  (testing "passar uma compra na lista inválida (sem o keyword de valor)"
      (is (thrown? clojure.lang.ExceptionInfo
                   (lista-valor-total-por-categoria [ex/compra2 ex/compra3 ex/compraSemKeyWordValor]))))

  (testing "passar uma lista vazia"
    (is (= #{} (lista-valor-total-por-categoria [])))
    (is (= #{} (lista-valor-total-por-categoria nil))))

  )

(deftest total-lista-compras-test
  (testing "não pode invocar total lista de compras sem simbolos obrigatórios"
    (is (thrown? clojure.lang.ExceptionInfo
                 (total-lista-de-compras [nil])))

    (is (thrown? clojure.lang.ExceptionInfo
                 (total-lista-de-compras [ex/compraComValorErradoAssociadoAoKeyword])))))

(deftest adicionar-compra-test
  (testing "passar uma compra com atributos corretos"
    (is (= [ex/compra1 ex/compra2 ex/compra3]
           (adicionar-compra ex/compra3 [ex/compra1 ex/compra2]))))

  (testing "tentar adicionar nil como compra"
    (is (thrown? clojure.lang.ExceptionInfo
                 (adicionar-compra nil [(db/todas-as-compras)]))))

  (testing "passar uma compra sem uma keyword (falta valor)"
    (is (thrown? clojure.lang.ExceptionInfo
                 (adicionar-compra ex/compraSemKeyWordValor [(db/todas-as-compras)]))))

  (testing "passar uma compra com um valor inválido para uma keyword (id-cartão não pode ser uma string)"
    (is (thrown? clojure.lang.ExceptionInfo
                 (adicionar-compra ex/compraComValorErradoAssociadoAoKeyword [(db/todas-as-compras)]))))


  (testing "passar uma lista de compras nil"
    (is (= [ex/compra3] (adicionar-compra ex/compra3 nil))))

  (testing "passar uma lista vazia"
    (is (= [ex/compra2] (adicionar-compra ex/compra2 []))))

  (testing "passar um dicionário no lugar da lista"
    (is (thrown? clojure.lang.ExceptionInfo
                 (adicionar-compra ex/compra1 {})))))
