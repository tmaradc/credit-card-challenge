(use 'java-time)

;--------------------------------- CLIENTE ---------------------------------
(def cliente {:nome "Tamara",
              :cpf "15426584799",
              :email "tamara@gmail.com"})

(println (:nome cliente))

;---------------------------- CARTÃO DE CRÉDITO ----------------------------
(def cartao-de-credito {:numero "2222 2222 2222 2222",
                        :cvv "222",
                        :validade "11/2029",
                        :limite 1500})

(println (:numero cartao-de-credito))

;---------------------------- LISTA DE COMPRAS -----------------------------
(def compra1 {:data (local-date-time 2021 11 18 9 39 30),
             :valor 25.50,
             :estabelecimento "Mercado Extra",
             :categoria "Alimentação"})

(def compra2 {:data (local-date-time 2021 11 20 10 50 00),
              :valor 90.50,
              :estabelecimento "Pacheco",
              :categoria "Saúde"})

(def compra3 {:data (local-date-time 2021 11 21 7 20 30),
              :valor 300.00,
              :estabelecimento "Cambly",
              :categoria "Educação"})

(def compra4 {:data (local-date-time 2021 11 22 9 39 30),
              :valor 30.50,
              :estabelecimento "Mercado Extra",
              :categoria "Alimentação"})

(println (:valor compra1))

(def lista-de-compras [compra1 compra2 compra3 compra4])

(println lista-de-compras)

;--------------------------- GASTOS POR CATEGORIA ----------------------------
(defn total-lista-de-compras
  [lista]
  (reduce + (map :valor lista)))

(defn total-por-categoria
  [[categoria lista-compras]]
  {:categoria categoria
   :gasto-total (total-lista-de-compras lista-compras)})

(defn lista-valor-total-por-categoria
  [lista-de-compras]
  (->> lista-de-compras
       (group-by :categoria)
       (map total-por-categoria)))

(println (lista-valor-total-por-categoria lista-de-compras))