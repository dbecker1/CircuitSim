package com.ra4king.circuitsimulator.components.gates;

import com.ra4king.circuitsimulator.Simulator;
import com.ra4king.circuitsimulator.WireValue;
import com.ra4king.circuitsimulator.WireValue.State;

/**
 * @author Roi Atalla
 */
public class NotGate extends Gate {
	public NotGate(Simulator simulator, String name, int bitSize) {
		super(simulator, "NOT", bitSize, 1);
	}
	
	@Override
	public void valueChanged(WireValue value, int portIndex) {
		if(portIndex == 1) return;
		
		WireValue result = new WireValue(value.getBitSize());
		for(int i = 0; i < result.getBitSize(); i++) {
			State bit = value.getBit(i);
			result.setBit(i, bit == State.X ? State.X : bit == State.ONE ? State.ZERO : State.ONE);
		}
		
		ports[1].pushValue(result);
	}
}