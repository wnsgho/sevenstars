import React, { useEffect, useState } from 'react'; // useState와 useEffect 임포트
import axios from 'axios'; // axios 임포트
import './App.css';
import 'bootstrap/dist/css/bootstrap.css';
import { ProductList } from './components/ProductList'; // ProductList 임포트
import { Summary } from './components/Summary'; // Summary 임포트

function App() {
  const [products, setProducts] = useState([]);
  const [items, setItems] = useState([]);
  const [page, setPage] = useState(1); // 현재 페이지
  const [size, setSize] = useState(10); // 한 페이지당 항목 수
  const [totalPages, setTotalPages] = useState(1); // 전체 페이지 수

  const handleAddClicked = (productId) => {
    const product = products.find((v) => v.productId === productId);
    const found = items.find((v) => v.productId === productId);
    const updatedItems = found
        ? items.map((v) =>
            v.productId === productId ? { ...v, count: v.count + 1 } : v
        )
        : [...items, { ...product, count: 1 }];
    setItems(updatedItems);
  };

  useEffect(() => {
    axios
        .get(`http://localhost:8080/api/v1/products?page=${page}&size=${size}`)
        .then((v) => {
          setProducts(v.data.content);
          setTotalPages(v.data.totalPages); // 전체 페이지 수 설정
        });
  }, [page, size]);

  const handleOrderSubmit = (order) => {
    if (items.length === 0) {
      alert('아이템을 추가해 주세요!');
    } else {
      axios
          .post('http://localhost:8080/api/v1/orders', {
            email: order.email,
            address: order.address,
            postcode: order.postcode,
            orderItems: items.map((v) => ({
              productId: v.productId,
              category: v.category,
              price: v.price,
              quantity: v.count,
            })),
          })
          .then(
              (v) => alert('주문이 정상적으로 접수되었습니다.'),
              (e) => {
                alert('서버 장애');
                console.error(e);
              }
          );
    }
  };

  // 페이지 변경 핸들러
  const handlePageChange = (newPage) => {
    if (newPage >= 1 && newPage <= totalPages) {
      setPage(newPage);
    }
  };

  return (
      <div className="container-fluid">
        <div className="row justify-content-center m-4">
          <h1 className="text-center">Grids & Circle</h1>
        </div>
        <div className="card">
          <div className="row">
            <div className="col-md-8 mt-4 d-flex flex-column align-items-start p-3 pt-0">
              <ProductList products={products} onAddClick={handleAddClicked} />
              {/* 페이지네이션 버튼 */}
              <div className="mt-3">
                <button
                    className="btn btn-outline-secondary me-2"
                    onClick={() => handlePageChange(page - 1)}
                    disabled={page === 1}
                >
                  이전
                </button>
                <span>
                {page} / {totalPages}
              </span>
                <button
                    className="btn btn-outline-secondary ms-2"
                    onClick={() => handlePageChange(page + 1)}
                    disabled={page === totalPages}
                >
                  다음
                </button>
              </div>
            </div>
            <div className="col-md-4 summary p-4">
              <Summary items={items} onOrderSubmit={handleOrderSubmit} />
            </div>
          </div>
        </div>
      </div>
  );
}

export default App;