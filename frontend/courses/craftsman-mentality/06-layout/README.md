# 06. 레이아웃과 포지셔닝

## 블록 요소와 인라인 요소의 차이

### 블록 요소 (Block-level Elements)

- 부모 요소의 가로폭 전체를 차지하며, 세로로 쌓임
- 기본적으로 새로운 줄에서 시작
- 너비(width), 높이(height), 여백(margin, border, padding) 등을 설정할 수 있음
- `div`, `p`, `h1`~`h6`, `ul`, `ol`, `li`, `form` 등이 해당

### 인라인 요소 (Inline-level Elements)

- 콘텐츠의 일부분만 차지하며, 요소가 포함한 콘텐츠의 크기만큼만 가로폭을 가짐
- 새로운 줄에서 시작하지 않고, 줄 중간에 추가
- 너비(width), 높이(height), 상하 여백(margin-top, margin-bottom, padding-top, padding-bottom), 밑줄(text-decoration) 등의 속성이 설정되더라도 무시될 수 있음
- `span`, `a`, `strong`, `em`, `img` 등이 해당

## display 속성 (요소의 표시 방식)

### 주요 display 속성 값

- `block`: 해당 요소를 블록 레벨 요소로 지정
- `inline`: 해당 요소를 인라인 요소로 지정
- `inline-block`: 인라인 요소처럼 취급되면서도 블록 레벨 요소처럼 너비와 높이를 설정 가능
- `none`: 해당 요소를 화면에서 숨김

### flexbox (일차원 또는 한 줄 레이아웃)

- 유연한 박스 모델을 통해 아이템을 배치하고 정렬하는 데 사용되는 CSS 레이아웃 방식
- 복잡한 레이아웃을 간단하게 처리할 수 있으며, 동적이고 유연한 디자인을 만들 수 있음
- 주로 한 줄의 레이아웃을 조절하는 데 사용, 수평 축 또는 수직 축 중 하나를 선택하여 아이템 배치
- `flex-direction: row` 또는 `flex-direction: column`

### grid (이차원 레이아웃)

- 행과 열을 사용하여 복잡한 레이아웃을 구성하는 데 사용
- grid를 사용하면 유연하고 강력한 레이아웃을 만들 수 있음
