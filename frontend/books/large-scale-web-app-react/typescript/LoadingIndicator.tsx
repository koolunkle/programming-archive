import React from 'react';

const statuss = {
    IDLE: 'IDLE',
    LOADING: 'LOADING',
    SUCCESS: 'SUCCESS',
    ERROR: 'ERROR',
} as const;

type Status = typeof statuss[keyof typeof statuss];

interface Props {
    status: Status;
}

const LoadingIndicator: React.FC<Props> = ({ status }) => {
    switch (status) {
        case statuss.IDLE:
            return null;
        case statuss.LOADING:
            return <div>Loading...</div>;
        case statuss.SUCCESS:
            return <div>Data loaded successfully</div>;
        case statuss.ERROR:
            return <div>Error loading data</div>;
        default:
            return null;
    }
}

export default LoadingIndicator;