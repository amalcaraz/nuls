package io.nuls.ledger.entity.utxoTransaction;

import io.nuls.core.constant.TransactionConstant;
import io.nuls.ledger.entity.tx.TransferTransaction;
import io.nuls.ledger.entity.UtxoData;
import io.nuls.ledger.entity.validator.UtxoTxInputsValidator;
import io.nuls.ledger.entity.validator.UtxoTxOutputsValidator;

/**
 * Created by Niels on 2017/11/14.
 */
public class UtxoTransferTransaction extends TransferTransaction<UtxoData> {

    public UtxoTransferTransaction() {
        super(TransactionConstant.TX_TYPE_TRANSFER);
        this.registerValidator(new UtxoTxInputsValidator());
        this.registerValidator(new UtxoTxOutputsValidator());
    }

}
