// Next.js의 확장된 가져오기 함수를 사용해 서버에서 데이터 가져오기

async function getProduct(id) {
    const res = await fetch(
        `https://api.example.com/products/${id}`
    );

    if (!res.ok) {
        throw new Error("Failed to fetch data")
    }

    const productData = await res.json();

    return productData;
}

async function ProductPage({ params }) {
    const product = await getProduct(params.id);
    return (
        <div>
            <h1>{product.name}</h1>
            <p>{product.description}</p>
        </div>
    )
}

export default ProductPage;